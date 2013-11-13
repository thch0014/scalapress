package com.cloudray.scalapress.item.attr

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/** @author Stephen Samuel */
@Component
@Autowired
class AttributeOptionRenameService(attributeValueDao: AttributeValueDao) {

  def rename(attribute: Attribute, oldValue: String, newValue: String) {
    attributeValueDao.updateValues(attribute: Attribute, oldValue: String, newValue: String)
  }
}
