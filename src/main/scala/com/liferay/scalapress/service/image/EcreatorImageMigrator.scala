package com.liferay.scalapress.service.image

import com.liferay.scalapress.Logging
import com.liferay.scalapress.service.asset.AssetStore
import java.io.ByteArrayInputStream
import java.net.URL
import java.util.concurrent.{TimeUnit, Executors, ExecutorService}
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.liferay.scalapress.dao.{ImageDao, WidgetDao, FolderDao}
import scala.collection.JavaConverters._
import com.liferay.scalapress.section.content.FolderContentSection
import actors.Futures

/** @author Stephen Samuel */
@Component
object EcreatorImageMigrator extends Logging {

    @Autowired var folderDao: FolderDao = _
    @Autowired var widgetDao: WidgetDao = _
    @Autowired var imageDao: ImageDao = _

    private def migrateImage(domain: String, file: String, store: AssetStore) {
        try {
            val normalizedDomain = domain.replace("http://", "")
            val url = "http://" + normalizedDomain + "/images/" + file
            logger.debug("Downloading url [{}]", url)

            val ba = IOUtils.toByteArray(new URL(url))
            val key = store.put(file, new ByteArrayInputStream(ba))

            logger.debug("Saved obj to key={}", key)

        } catch {
            case e: Exception => logger.error("{}", e)
        }
    }

    def migrateCategories(domain: String, store: AssetStore, executor: ExecutorService) {
        folderDao.findAll().foreach(arg => {
            executor.submit(new Runnable() {
                def run() {
                    arg.sections.asScala
                      .filter(_.isInstanceOf[FolderContentSection])
                      .map(_.asInstanceOf[FolderContentSection])
                      .map(_.getContent)
                      .foreach(content => {
                        val matches = "<img[^>]*src=\"images/(.*?)\"".r.findAllIn(content)
                        matches.matchData.map(arg => {
                            logger.debug("Found general image {}", arg.group(1))
                            arg.group(1)
                        }).foreach(migrateImage(domain, _, store))
                    })
                }
            })
        })
    }

    def migrateImages(domain: String, store: AssetStore, executor: ExecutorService) {
        imageDao.findAll().filter(_.getFilename != null).foreach(arg => {
            executor.submit(new Runnable() {
                def run() {
                    migrateImage(domain, arg.getFilename, store)
                }
            })
        })
    }

    def migrateSideboxes(domain: String, store: AssetStore, executor: ExecutorService) {
        widgetDao.findAll()
          .filter(_.isInstanceOf[FolderContentSection])
          .map(_.asInstanceOf[FolderContentSection])
          .foreach(arg => {
            executor.submit(new Runnable() {
                def run() {
                    val content = arg.getContent
                    val matches = "<img[^>]*src=\"images/(.*?)\"".r.findAllIn(content)
                    matches.matchData.map(arg => {
                        logger.debug("Found sidebox image {}", arg.group(1))
                        arg.group(1)
                    }).foreach(migrateImage(domain, _, store))
                }
            })
        })
    }

    def migrate(domain: String, assetStore: AssetStore) {
        require(domain != null)
        require(assetStore != null)
        logger.info("Starting migration on {}", domain)

        val executor = Executors.newFixedThreadPool(8)
        logger.info("Created 8 thread pool for migrating images")


        migrateImages(domain, assetStore, executor)
        migrateSideboxes(domain, assetStore, executor)
        migrateCategories(domain, assetStore, executor)

        Futures.future {
            executor.shutdown()
            executor.awaitTermination(1, TimeUnit.HOURS)
            logger.info("Migration completed")
        }
    }
}
