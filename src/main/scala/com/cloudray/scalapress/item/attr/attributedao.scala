package com.cloudray.scalapress.item.attr

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait AttributeDao extends GenericDao[Attribute, java.lang.Long]

@Component
@Transactional
class AttributeDaoImpl extends GenericDaoImpl[Attribute, java.lang.Long] with AttributeDao

trait AttributeOptionDao extends GenericDao[AttributeOption, java.lang.Long]

@Component
@Transactional
class AttributeOptionDaoImpl extends GenericDaoImpl[AttributeOption, java.lang.Long] with AttributeOptionDao

trait AttributeValueDao extends GenericDao[AttributeValue, java.lang.Long] {
  def updateValues(attribute: Attribute, oldValue: String, newValue: String): Int
}

@Component
@Transactional
class AttributeValueDaoImpl extends GenericDaoImpl[AttributeValue, java.lang.Long] with AttributeValueDao {
  def updateValues(attribute: Attribute, oldValue: String, newValue: String): Int = {
    getSession
      .createSQLQuery("UPDATE attributes_values SET value=? WHERE value=? AND attribute=?")
      .setString(0, oldValue)
      .setString(1, newValue)
      .setLong(2, attribute.id)
      .executeUpdate()
  }
}
