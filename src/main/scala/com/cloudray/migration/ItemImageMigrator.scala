package com.cloudray.migration

import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.media.{MediaWidget, Image}
import javax.annotation.PostConstruct
import com.cloudray.scalapress.plugin.gallery.galleryview.GalleryDao
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Component
import com.cloudray.scalapress.framework.{Logging, ScalapressContext}

/** @author Stephen Samuel */
@Component
class ItemImageMigrator extends Logging {

  @Autowired var context: ScalapressContext = _
  @Autowired var galleryDao: GalleryDao = _

  @Transactional
  def migrate(image: Image) {
    logger.debug("Migrating image: {}", image.filename)

    if (image.item != null) {

      val obj = context.itemDao.find(image.item.toLong)
      if (obj != null) {
        if (!obj.images.contains(image.filename)) {
          obj.images.add(image.filename)
          context.itemDao.save(obj)
        }
      }

    } else if (image.imageBox != null) {

      val widget = context.widgetDao.find(image.imageBox.toLong).asInstanceOf[MediaWidget]
      if (widget != null) {
        widget.images.add(image.filename)
        context.widgetDao.save(widget)
      }

    } else if (image.gallery != null) {

      val gallery = galleryDao.find(image.gallery.toLong)
      if (gallery != null) {
        gallery.images.add(image.filename)
        galleryDao.save(gallery)
      }
    }
  }

  @PostConstruct
  def run() {
    logger.debug("Beginning migration run [{}]", context.installationDao.get.name)
    val images = context.imageDao.findAll.sortBy(_.id).sortBy(_.position)
    logger.info("Images [{}]", images.size)
    images.foreach(image =>
      try {
        migrate(image)
      } catch {
        case e: Exception =>
      })
    images.foreach(context.imageDao.remove)
  }
}
