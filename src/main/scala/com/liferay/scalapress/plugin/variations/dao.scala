package com.liferay.scalapress.plugin.variations

import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */
trait DimensionDao extends GenericDao[Dimension, java.lang.Long]

@Component
@Transactional
class DimensionDaoImpl extends GenericDaoImpl[Dimension, java.lang.Long] with DimensionDao