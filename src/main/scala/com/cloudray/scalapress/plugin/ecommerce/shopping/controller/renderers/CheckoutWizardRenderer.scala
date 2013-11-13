package com.cloudray.scalapress.plugin.ecommerce.controller.renderers

import com.cloudray.scalapress.util.{WizardStep, WizardRenderer}

/** @author Stephen Samuel */
object CheckoutWizardRenderer {

  object AddressStep extends WizardStep("/checkout/address", "Address")
  object DeliveryStep extends WizardStep("/checkout/delivery", "Delivery")
  object ConfirmationStep extends WizardStep("/checkout/confirmation", "Confirmation")
  object PaymentStep extends WizardStep("/checkout/payment", "Payment")
  object CompletionStep extends WizardStep("#", "Completed")

  lazy val steps = List(AddressStep, DeliveryStep, ConfirmationStep, PaymentStep, CompletionStep)
  def render(active: WizardStep) = WizardRenderer.render(steps, active)
}
