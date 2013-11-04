package com.cloudray.scalapress.plugin.ecommerce

import com.cloudray.scalapress.framework.{ScalapressContext, RegistrationLinkProvider}
import com.cloudray.scalapress.plugin.ecommerce.dao.DeliveryOptionDao

/** @author Stephen Samuel */
class BasketRegistrationLinkProvider extends RegistrationLinkProvider {

  def enabled(context: ScalapressContext) = context.bean[DeliveryOptionDao].findAll.size > 0
  def text: String = "Basket"
  def description = "View your basket"
  def link: String = "/basket"
}
