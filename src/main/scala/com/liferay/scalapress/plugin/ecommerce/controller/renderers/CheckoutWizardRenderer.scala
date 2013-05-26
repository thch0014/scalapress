package com.liferay.scalapress.plugin.ecommerce.controller.renderers

import com.liferay.scalapress.util.{WizardStep, WizardRenderer}

/** @author Stephen Samuel */
object CheckoutWizardRenderer {

    val AddressStage = 1
    val DeliveryStage = 2
    val STEP_CONFIRMATION = 3
    val STEP_PAYMENT = 4
    val STEP_COMPLETED = 5

    def steps = List(WizardStep("/checkout/address", "Address"),
        WizardStep("/checkout/delivery", "Delivery"),
        WizardStep("/checkout/confirmation", "Confirmation"),
        WizardStep("/checkout/payment", "Payment"),
        WizardStep("#", "Completed"))

    def render(stage: Int) = WizardRenderer.render(steps, stage)

}
