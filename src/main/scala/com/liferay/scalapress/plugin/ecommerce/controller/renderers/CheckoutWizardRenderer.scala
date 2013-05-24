package com.liferay.scalapress.plugin.ecommerce.controller.renderers

import com.liferay.scalapress.util.{WizardStep, WizardRenderer}

/** @author Stephen Samuel */
object CheckoutWizardRenderer {

    val AddressStage = 1
    val DeliveryStage = 2
    val ConfirmationStage = 3
    val CompletedStage = 4

    def steps = List(WizardStep("/checkout/address", "Address"),
        WizardStep("/checkout/delivery", "Delivery"),
        WizardStep("/checkout/payment", "Transaction"),
        WizardStep("#", "Completed"))

    def render(stage: Int) = WizardRenderer.render(steps, stage)

}
