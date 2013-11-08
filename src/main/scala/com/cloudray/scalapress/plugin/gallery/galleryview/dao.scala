package com.cloudray.scalapress.plugin.gallery.galleryview

import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.plugin.gallery.base.Gallery

/** @author Stephen Samuel */
trait GalleryDao extends GenericDao[Gallery, java.lang.Long]

@Component
@Transactional
class GalleryDaoImpl extends GenericDaoImpl[Gallery, java.lang.Long] with GalleryDao
