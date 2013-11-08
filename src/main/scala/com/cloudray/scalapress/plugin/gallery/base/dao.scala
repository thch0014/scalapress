package com.cloudray.scalapress.plugin.gallery.base

import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */
trait GalleryDao extends GenericDao[Gallery, java.lang.Long]

@Component
@Transactional
class GalleryDaoImpl extends GenericDaoImpl[Gallery, java.lang.Long] with GalleryDao
