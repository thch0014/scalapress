package com.liferay.scalapress.plugin.payments.paypal.standard

import javax.persistence.{Entity, Table}
import reflect.BeanProperty
import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.plugin.payments.{PaymentPluginDao, FormPaymentProcessor, PaymentPlugin}
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_paypal_standard")
class PaypalStandardPlugin extends PaymentPlugin {

    @BeanProperty var accountEmail: String = _

    //  def backofffice = "backoffice/plugin/payment/paypal/standard"
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