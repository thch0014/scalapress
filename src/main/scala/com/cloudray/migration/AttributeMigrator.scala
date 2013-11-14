package com.cloudray.migration

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.item.attr.{AttributeOption, AttributeType, AttributeDao}
import javax.annotation.PostConstruct

/** @author Stephen Samuel */
@Component
@Autowired
class AttributeMigrator(attributeDao: AttributeDao) {

  @PostConstruct
  def run() {
    attributeDao.findAll.foreach(attr => {
      attr.attributeType match {
        case AttributeType.Boolean =>
          attr.attributeType = AttributeType.Selection

          val yes = new AttributeOption
          yes.attribute = attr
          yes.position = 1
          yes.value = "Yes"

          val no = new AttributeOption
          no.attribute = attr
          no.position = 1
          no.value = "No"

          attr.options.add(yes)
          attr.options.add(no)

          attributeDao.save(attr)

        case _ =>
      }
    })
  }
}
