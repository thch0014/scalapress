package com.liferay.scalapress.dao.ecommerce

import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import com.liferay.scalapress.domain.ecommerce.ShoppingPlugin
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
trait ShoppingPluginDao extends GenericDao[ShoppingPlugin, java.lang.Long]

@Component
@Transactional
class ShoppingPluginDaoImpl extends GenericDaoImpl[ShoppingPlugin, java.lang.Long] with ShoppingPluginDao

@Component
class ShoppingPluginDaoValidator {
    @Autowired var shoppingPluginDao: ShoppingPluginDao = _
    @PostConstruct def ensureOne() {
        if (shoppingPluginDao.findAll().size == 0) {
            val plugin = new ShoppingPlugin
            shoppingPluginDao.save(plugin)
        }
    }
}