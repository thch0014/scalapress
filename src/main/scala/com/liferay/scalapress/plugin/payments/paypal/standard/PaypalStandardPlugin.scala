package com.liferay.scalapress.plugin.payments.paypal.standard

import javax.persistence.{Entity, Table}
import reflect.BeanProperty
import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import com.liferay.scalapress.plugin.payments.{FormPaymentProcessor, PaymentPlugin}

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_paypalstandard")
class PaypalStandardPlugin extends PaymentPlugin {

    @BeanProperty var accountEmail: String = _

    def backofffice = "backoffice/plugin/payment/paypal/standard"
    def name: String = "Paypal Standard"
    def processor: FormPaymentProcessor = new PaypalStandardProcessor(this)
    def enabled = Option(accountEmail).filter(_.trim.length > 0).isDefined
}

trait PaypalStandardPluginDao extends GenericDao[PaypalStandardPlugin, java.lang.Long] {
    def get: PaypalStandardPlugin
}

@Component
@Transactional
class PaypalStandardPluginDaoImpl
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
