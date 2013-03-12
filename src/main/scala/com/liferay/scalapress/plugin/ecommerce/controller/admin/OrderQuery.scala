package com.liferay.scalapress.plugin.ecommerce.controller.admin

import com.sksamuel.scoot.soa.PagedQuery

/** @author Stephen Samuel */
case class OrderQuery(status: Option[String] = None) extends PagedQuery
