package com.cloudray.migration

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import org.hibernate.Session

/** @author Stephen Samuel */
trait ImageDao extends GenericDao[Image, java.lang.Long] {
  def getSession: Session
}

@Component
@Transactional
class ImageDaoImpl extends GenericDaoImpl[Image, java.lang.Long] with ImageDao {
  override def getSession: Session = super.getSession
}