package com.cloudray.migration.ecreator

import java.util.concurrent.{TimeUnit, Executors, ExecutorService}
import com.cloudray.scalapress.item.ItemDao
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.media.{AssetStore}
import com.cloudray.scalapress.widgets.{HtmlWidget, WidgetDao}
import com.cloudray.scalapress.folder.FolderDao
import java.io.ByteArrayInputStream
import org.apache.commons.io.IOUtils
import java.net.URL
import scala.collection.JavaConverters._
import scala.concurrent._
import com.cloudray.scalapress.folder.section.FolderContentSection
import scala.concurrent.ExecutionContext.Implicits.global
import org.springframework.stereotype.Component
import com.cloudray.scalapress.framework.Logging
import com.cloudray.migration.ImageDao

/** @author Stephen Samuel */
@Component
class ECImageMigrator extends Logging {

  @Autowired var folderDao: FolderDao = _
  @Autowired var widgetDao: WidgetDao = _
  @Autowired var imageDao: ImageDao = _
  @Autowired var store: AssetStore = _
  @Autowired var objectDao: ItemDao = _

  private def migrateImage(domain: String, file: String, store: AssetStore) {
    try {
      val normalizedDomain = domain.replace("http://", "")
      val url = "http://" + normalizedDomain + "/images/" + file
      logger.info("Downloading url [{}]", url)

      val ba = IOUtils.toByteArray(new URL(url))
      val key = store.put(file, new ByteArrayInputStream(ba))

      logger.debug("Saved item to key={}", key)

    } catch {
      case e: Exception => logger.error("{}", e)
    }
  }

  def migrateContent(domain: String, content: String, executor: ExecutorService) {
    if (content != null) {
      if (content.contains("1-marquee-logo-grey_blue.jpg")) {
        println("hello")
      }
      val matches = "<img[^>]+src=\"/?images/(.*?)\"".r.findAllIn(content)
      matches.matchData.map(arg => {
        logger.info("Found content image {}", arg.group(1))
        arg.group(1)
      }).foreach(migrateImage(domain, _, store))
    }
  }

  def migrateCategories(domain: String, executor: ExecutorService) {
    val folders = folderDao.findAll
    logger.info("Migrating {} folders", folders.size)
    folders.foreach(arg => {
      val sections = arg.sections.asScala
      logger.info("{} sections", sections.size)
      executor.submit(new Runnable() {
        def run() {
          sections
            .filter(_.isInstanceOf[FolderContentSection])
            .map(_.asInstanceOf[FolderContentSection])
            .foreach(s => migrateContent(domain, s.content, executor))
          Option(arg.header).foreach(content => migrateContent(domain, content, executor))
          Option(arg.footer).foreach(content => migrateContent(domain, content, executor))
        }
      })
    })
  }

  def migrateImages(domain: String, executor: ExecutorService) {
    val images = imageDao.findAll
    logger.info("Migrating {} images", images.size)
    images.filter(_.getFilename != null).foreach(arg => {
      executor.submit(new Runnable() {
        def run() {
          migrateImage(domain, arg.getFilename, store)
        }
      })
    })
  }

  def migrateSideboxes(domain: String, executor: ExecutorService) {
    val widgets = widgetDao.findAll
    logger.info("Migrating {} widgets", widgets.size)
    widgets.filter(_.isInstanceOf[HtmlWidget])
      .map(_.asInstanceOf[HtmlWidget])
      .foreach(arg => {
      executor.submit(new Runnable() {
        def run() {
          val content = arg.getContent
          val matches = "<img[^>]*src=\"images/(.*?)\"".r.findAllIn(content)
          matches.matchData.map(arg => {
            logger.info("Found sidebox image {}", arg.group(1))
            arg.group(1)
          }).foreach(migrateImage(domain, _, store))
        }
      })
    })
  }

  def migrateItems(domain: String, executor: ExecutorService) {
    val objects = objectDao.findAll
    logger.info("Migrating {} objects", objects.size)
    objects.foreach(arg => {
      executor.submit(new Runnable() {
        def run() {
          Option(arg.content).foreach(content => migrateContent(domain, content, executor))
        }
      })
    })
  }

  def migrate(domain: String) {
    require(domain != null)
    logger.info("Starting migration on {}", domain)

    val executor = Executors.newFixedThreadPool(8)
    logger.info("Created 8 thread pool for migrating images")

    migrateImages(domain, executor)
    migrateSideboxes(domain, executor)
    migrateCategories(domain, executor)
    migrateItems(domain, executor)

    future {
      executor.shutdown()
      executor.awaitTermination(1, TimeUnit.HOURS)
      logger.info("Migration completed, asset store contains {} objects", store.count)
    }
  }
}
