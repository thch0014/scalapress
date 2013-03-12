package com.liferay.scalapress.plugin.ecommerce.controller.admin

import com.liferay.scalapress.{Page, PagedQuery}

/** @author Stephen Samuel */
case class OrderQuery(status: Option[String] = None) extends PagedQuery
