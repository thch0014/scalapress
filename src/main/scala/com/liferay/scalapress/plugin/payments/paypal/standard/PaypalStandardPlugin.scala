package com.liferay.scalapress.plugin.payments.paypal.standard

import javax.persistence.{Entity, Table, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty
import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_paypalstandard")
class PaypalStandardPlugin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var accountEmail: String = _
}

trait PaypalStandardPluginDao extends GenericDao[PaypalStandardPlugin, java.lang.Long] {
    def get: PaypalStandardPlugin
}

@Component
@Transactional
class SagepayFormPluginDaoImpl
  extends GenericDaoImpl[PaypalStandardPlugin, java.lang.Long] with PaypalStandardPluginDao {
    def get = findAll.head
}

@Component
class PaypalStandardPluginDaoImplValidator {
    @Autowired var dao: PaypalStandardPluginDao = _
    @PostConstruct def ensureOne() {
        if (dao.findAll().size == 0) {
            val plugin = new PaypalStandardPlugin
            dao.save(plugin)
        }
    }
}
