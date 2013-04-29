package com.liferay.scalapress.plugin.ecommerce.dao

import com.liferay.scalapress.plugin.ecommerce.domain.DeliveryOption
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}
import java.lang

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