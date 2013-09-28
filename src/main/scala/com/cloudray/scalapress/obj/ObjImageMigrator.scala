package com.cloudray.scalapress.obj

import com.cloudray.scalapress.{Logging, ScalapressContext}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.media.{MediaWidget, Image}
import javax.annotation.PostConstruct
import org.springframework.stereotype.Component
import com.cloudray.scalapress.plugin.gallery.galleryview.GalleryDao
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */
@Component
class ObjImageMigrator extends Logging {

  @Autowired var context: ScalapressContext = _
  @Autowired var galleryDao: GalleryDao = _

  @Transactional
  def migrate(image: Image) {
    logger.debug("Migrating image: {}", image.filename)

    if (image.item != null) {

      //  logger.debug("object {}", image.item)
      val obj = context.objectDao.find(image.item.toLong)

      //    logger.debug("images before {}", obj.images.size)

      if (!obj.images.contains(image.filename)) {
        obj.images.add(image.filename)
        context.objectDao.save(obj)
      }

      //     logger.debug("images after {}", obj.images.size)

    } else if (image.imageBox != null) {

      val widget = context.widgetDao.find(image.imageBox.toLong).asInstanceOf[MediaWidget]
      if (widget != null) {
        widget.images.add(image.filename)
        context.widgetDao.save(widget)
      }

    } else if (image.gallery != null) {

      val gallery = galleryDao.find(image.gallery.toLong)

      gallery.images.add(image.filename)
      galleryDao.save(gallery)
    }
  }

  @PostConstruct
  def run() {
    logger.debug("Beginning migration run [{}]", context.installationDao.get.name)
    val images = context.imageDao.findAll().sortBy(_.id).sortBy(_.position)
    logger.info("Images [{}]", images.size)
    images.foreach(migrate)
    images.foreach(context.imageDao.remove)
  }
}
