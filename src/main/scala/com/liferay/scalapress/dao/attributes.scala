package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import com.liferay.scalapress.domain.attr.{AttributeOption, AttributeValue, Attribute}
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */
trait AttributeDao extends GenericDao[Attribute, java.lang.Long]

@Component
@Transactional
class AttributeDaoImpl extends GenericDaoImpl[Attribute, java.lang.Long] with AttributeDao

trait AttributeOptionDao extends GenericDao[AttributeOption, java.lang.Long]

@Component
@Transactional
class AttributeOptionDaoImpl extends GenericDaoImpl[AttributeOption, java.lang.Long] with AttributeOptionDao

trait AttributeValueDao extends GenericDao[AttributeValue, java.lang.Long]

@Component
@Transactional
class AttributeValueImpl extends GenericDaoImpl[AttributeValue, java.lang.Long] with AttributeValueDao
