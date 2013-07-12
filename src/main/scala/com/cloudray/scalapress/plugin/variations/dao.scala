package com.cloudray.scalapress.plugin.variations

import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */
trait DimensionDao extends GenericDao[Dimension, java.lang.Long]

@Component
@Transactional
class DimensionDaoImpl extends GenericDaoImpl[Dimension, java.lang.Long] with DimensionDao

trait VariationDao extends GenericDao[Variation, java.lang.Long]

@Component
@Transactional
class VariationDaoImpl extends GenericDaoImpl[Variation, java.lang.Long] with VariationDao