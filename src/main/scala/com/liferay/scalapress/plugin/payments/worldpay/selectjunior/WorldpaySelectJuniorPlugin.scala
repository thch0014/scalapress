package com.liferay.scalapress.plugin.payments.worldpay.selectjunior

import javax.persistence.{Table, Entity}
import com.liferay.scalapress.plugin.payments.{FormPaymentProcessor, PaymentPlugin}

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_payment_worldpay_selectjunior")
class WorldpaySelectJuniorPlugin extends PaymentPlugin {

    var live: Boolean = _
    var installationId: String = _
    var accountId: String = _
    var authMode: String = _
    var callbackPassword: String = _

    def enabled: Boolean = live
    def processor: FormPaymentProcessor = new WorldpaySelectJuniorProcessor(this)
    def name: String = "Worldpay Select Junior"
}
