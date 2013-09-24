package com.cloudray.scalapress.obj

import com.cloudray.scalapress.{Logging, ScalapressContext}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.media.Image
import javax.annotation.PostConstruct
import org.springframework.stereotype.Component

/** @author Stephen Samuel */
@Component
class ObjImageMigrator extends Logging {

  @Autowired var context: ScalapressContext = _

  def migrate(image: Image) {
    logger.debug("Migrating image: {}", image.filename)
    if (image.obj != null) {
      if (!image.obj.images.contains(image.filename)) {
        image.obj.images.add(image.filename)
        context.objectDao.save(image.obj)
        context.imageDao.remove(image)
      }
    }
  }

  @PostConstruct
  def run() {
    logger.debug("Beginning migration run [{}]", context.installationDao.get.name)
    val images = context.imageDao.findAll().sortBy(_.id).sortBy(_.position)
    for ( image <- images ) migrate(image)
  }
}
