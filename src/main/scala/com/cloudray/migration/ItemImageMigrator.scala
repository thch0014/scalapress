package com.cloudray.migration

import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.media.MediaWidget
import javax.annotation.PostConstruct
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.framework.Logging
import com.cloudray.scalapress.widgets.WidgetDao
import com.cloudray.scalapress.item.ItemDao
import com.cloudray.scalapress.settings.InstallationDao
import com.cloudray.scalapress.plugin.gallery.base.{GalleryDao, Image}

/** @author Stephen Samuel */
class ItemImageMigrator extends Logging {

  @Autowired var widgetDao: WidgetDao = _
  @Autowired var imageDao: ImageDao = _
  @Autowired var itemDao: ItemDao = _
  @Autowired var galleryDao: GalleryDao = _
  @Autowired var installationDao: InstallationDao = _

  @Transactional
  def migrate(image: Image) {
    logger.debug("Migrating image: {}", image.filename)

    if (image.item != null) {

      val item = itemDao.find(image.item.toLong)
      if (item != null) {
        if (!item.images.contains(image.filename)) {
          item.images.add(image.filename)
          itemDao.save(item)
        }
      }

    } else if (image.imageBox != null) {

      val widget = widgetDao.find(image.imageBox.toLong).asInstanceOf[MediaWidget]
      if (widget != null) {
        widget.images.add(image.filename)
        widgetDao.save(widget)
      }

    } else if (image.gallery != null) {

      val gallery = galleryDao.find(image.gallery.toLong)
      if (gallery != null) {
        gallery.images.add(Image(image.filename, null))
        galleryDao.save(gallery)
      }
    }
  }

  @PostConstruct
  def run() {
    logger.debug("Beginning migration run [{}]", installationDao.get.name)
    val images = imageDao.findAll.sortBy(_.id).sortBy(_.position)
    logger.info("Images [{}]", images.size)
    images.foreach(image =>
      try {
        migrate(image)
      } catch {
        case e: Exception =>
      })
    images.foreach(imageDao.remove)
  }
}
