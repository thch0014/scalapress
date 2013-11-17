package com.cloudray.scalapress.plugin.ecommerce.shopping.dao

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import java.lang
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.DeliveryOption

/** @author Stephen Samuel */
trait DeliveryOptionDao extends GenericDao[DeliveryOption, java.lang.Long]

@Component
@Transactional
class DeliveryOptionDaoImpl extends GenericDaoImpl[DeliveryOption, java.lang.Long] with DeliveryOptionDao {
  override def remove(del: DeliveryOption): Boolean = {
    del.deleted = 1
    save(del)
  }
  override def removeById(id: lang.Long): Boolean = {
    val del = find(id)
    del.deleted = 1
    save(del)
  }
  override def findAll = super.findAll.filter(_.deleted == 0)
}