package com.cloudray.scalapress.plugin.ecommerce.shopping

import com.cloudray.scalapress.framework.{ScalapressContext, RegistrationLinkProvider}
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.DeliveryOptionDao

/** @author Stephen Samuel */
class CheckoutRegistrationLinkProvider extends RegistrationLinkProvider {

  def enabled(context: ScalapressContext) = context.bean[DeliveryOptionDao].findAll.size > 0
  def text: String = "Checkout"
  def description = "Go to the checkout"
  def link: String = "/checkout"
}
