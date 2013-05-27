package com.liferay.scalapress.plugin.payments.paypal.standard

import javax.persistence.{Entity, Table}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}
import scala.beans.BeanProperty
import com.liferay.scalapress.payments.{PaymentProcessor, PaymentPlugin}

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_paypal_standard")
class PaypalStandardPlugin extends PaymentPlugin {

    @BeanProperty var accountEmail: String = _

    @BeanProperty var production: Boolean = _

    //  def backofffice = "backoffice/plugin/payment/paypal/standard"
    def name: String = "Paypal"
    def processor: PaymentProcessor = new PaypalStandardProcessor(this)
    def enabled = Option(accountEmail).filterNot(_.isEmpty).isDefined
}

trait PaypalStandardPluginDao extends GenericDao[PaypalStandardPlugin, java.lang.Long] {
    def get: PaypalStandardPlugin
}

@Component
@Transactional
class PaypalStandardPluginDaoImpl extends GenericDaoImpl[PaypalStandardPlugin, java.lang.Long] with PaypalStandardPluginDao {
    def get = findAll.head
}