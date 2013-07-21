package com.cloudray.scalapress.media

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait ImageDao extends GenericDao[Image, java.lang.Long]

@Component
@Transactional
class ImageDaoImpl extends GenericDaoImpl[Image, java.lang.Long] with ImageDao
