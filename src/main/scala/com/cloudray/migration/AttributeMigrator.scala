package com.cloudray.migration

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.item.attr.{AttributeOption, AttributeType, AttributeDao}
import javax.annotation.PostConstruct
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */
@Component
@Autowired
class AttributeMigrator(attributeDao: AttributeDao) extends Runnable {

  @PostConstruct
  @Transactional
  def run() {
    attributeDao.findAll.foreach(attr => {
      attr.attributeType match {
        case AttributeType.Boolean =>

          val yes = new AttributeOption
          yes.attribute = attr
          yes.position = 1
          yes.value = "Yes"

          val no = new AttributeOption
          no.attribute = attr
          no.position = 1
          no.value = "No"

          import scala.collection.JavaConverters._
          attr.options = List(yes, no).asJava
          attr.attributeType = AttributeType.Selection
          attributeDao.save(attr)

        case _ =>
      }
    })
  }
}
