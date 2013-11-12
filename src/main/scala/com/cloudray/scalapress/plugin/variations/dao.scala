package com.cloudray.scalapress.plugin.variations

import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.googlecode.genericdao.search.Search

/** @author Stephen Samuel */
trait DimensionDao extends GenericDao[Dimension, java.lang.Long] {
  def findByItemTypeId(id: Long): Seq[Dimension]
}

@Component
@Transactional
class DimensionDaoImpl extends GenericDaoImpl[Dimension, java.lang.Long] with DimensionDao {
  def findByItemTypeId(id: Long): Seq[Dimension] =
    findAll.filterNot(_.objectType == null).filter(_.objectType.id == id)
}

trait VariationDao extends GenericDao[Variation, java.lang.Long] {
  def findByItemId(l: Long): Seq[Variation]
}

@Component
@Transactional
class VariationDaoImpl extends GenericDaoImpl[Variation, java.lang.Long] with VariationDao {
  def findByItemId(l: Long): Seq[Variation] = {
    search(new Search(classOf[Variation]).addFilterEqual("obj.id", l))
  }
}

trait DimensionValueDao extends GenericDao[DimensionValue, java.lang.Long] {
  def findByDimension(id: Long): Seq[DimensionValue]
}

@Component
@Transactional
class DimensionValueDaoImpl extends GenericDaoImpl[DimensionValue, java.lang.Long] with DimensionValueDao {
  def findByDimension(id: Long): Seq[DimensionValue] = {
    search(new Search(classOf[DimensionValue]).addFilterEqual("dimension.id", id))
  }
}