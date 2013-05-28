package com.cloudray.scalapress.plugin.payments.worldpay.selectjunior

import javax.persistence.{Table, Entity}
import com.cloudray.scalapress.payments.{PaymentProcessor, PaymentPlugin}

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_payment_worldpay_selectjunior")
class WorldpaySelectJuniorPlugin extends PaymentPlugin {

    var live: Boolean = _
    var installationId: String = _
    var accountId: String = _
    var authMode: String = _
    var callbackPassword: String = _

    def enabled: Boolean = accountId != null
    def processor: PaymentProcessor = new WorldpaySelectJuniorProcessor(this)
    def name: String = "Worldpay Select Junior"
}
