package com.cloudray.scalapress.plugin.ecommerce.controller.renderers

import org.fusesource.scalate.TemplateEngine

/** @author Stephen Samuel */
object OrderStatusRenderer {
  val engine = new TemplateEngine
  def form = engine.layout("/com/cloudray/scalapress/plugin/ecommerce/order_status.ssp")
}
