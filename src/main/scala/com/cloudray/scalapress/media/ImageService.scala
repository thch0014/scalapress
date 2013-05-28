package com.cloudray.scalapress.media

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.apache.commons.io.{IOUtils, FilenameUtils}
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import com.cloudray.scalapress.Logging
import javax.annotation.{PreDestroy, PostConstruct}
import java.util.concurrent.Executors

/** @author Stephen Samuel */
@Component
class ImageService extends Logging {

    val executor = Executors.newFixedThreadPool(4)
    val cache = scala.collection.mutable.HashSet[String]()
    @Autowired var assetStore: AssetStore = _

    @PreDestroy
    def destroy() {
        executor.shutdownNow()
    }

    @PostConstruct
    def populateCache() {
        assetStore.list(50000).foreach(asset => cache.add(asset.filename))
    }

    def imageLink(filename: String, w: Int, h: Int) = {
        require(w < 2000)
        require(h < 2000)
        executor.submit(new Runnable() {
            def run() {
                try {
                    _ensureThumbnailStored(filename, w, h)
                } catch {
                    case e: Exception => logger.warn(e.toString)
                }
            }
        })
        assetStore.link(_thumbailFilename(filename, w, h))
    }

    private def _thumbailFilename(filename: String, w: Int, h: Int): String =
        FilenameUtils.getBaseName(filename) + "___" + w + "x" + h + ".png"

    private def _ensureThumbnailStored(filename: String, w: Int, h: Int) {
        val thumbnailFilename = _thumbailFilename(filename, w, h)
        if (!cache.contains(thumbnailFilename)) {
            if (!assetStore.exists(thumbnailFilename)) {
                _readAssetAndCreateThumbnail(filename, w, h) match {
                    case None =>
                    case Some(thumb) => _storeImage(thumbnailFilename, thumb)
                }
            }
            cache.add(thumbnailFilename)
        }
    }

    private def _storeImage(filename: String, image: BufferedImage) {
        val bout = new ByteArrayOutputStream()
        ImageIO.write(image, "PNG", bout)
        assetStore.put(filename, new ByteArrayInputStream(bout.toByteArray))
    }

    private def _readAssetAndCreateThumbnail(key: String, w: Int, h: Int): Option[BufferedImage] = {
        logger.debug("Creating thumbnail for {}", key)
        assetStore.get(key) match {
            case Some(in) =>
                val bytes = IOUtils.toByteArray(in)
                try {
                    val source = ImageIO.read(new ByteArrayInputStream(bytes))
                    Option(ImageTools.fit(source, (w, h)))
                } catch {
                    case e: Exception =>
                        logger.warn("{}", e)
                        None
                }

            case None => None
        }
    }
}