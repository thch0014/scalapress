package com.cloudray.scalapress.plugin.variations

import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.googlecode.genericdao.search.Search

/** @author Stephen Samuel */
trait DimensionDao extends GenericDao[Dimension, java.lang.Long]

@Component
@Transactional
class DimensionDaoImpl extends GenericDaoImpl[Dimension, java.lang.Long] with DimensionDao

trait VariationDao extends GenericDao[Variation, java.lang.Long] {
  def findByObjectId(l: Long): Seq[Variation]
}

@Component
@Transactional
class VariationDaoImpl extends GenericDaoImpl[Variation, java.lang.Long] with VariationDao {
  def findByObjectId(l: Long): Seq[Variation] = {
    search(new Search(classOf[Variation]).addFilterEqual("obj.id", l))
  }
}