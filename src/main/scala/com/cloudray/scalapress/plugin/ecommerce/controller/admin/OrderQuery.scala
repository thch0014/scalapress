package com.cloudray.scalapress.plugin.ecommerce.controller.admin

import com.sksamuel.scoot.soa.PagedQuery

/** @author Stephen Samuel */
class OrderQuery extends PagedQuery {

    var orderId: Option[String] = None
    var status: Option[String] = None
    var name: Option[String] = None
    var from: Option[Long] = None
    var to: Option[Long] = None
}
