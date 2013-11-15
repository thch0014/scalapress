package com.cloudray.scalapress.util

import scala.xml.{Node, Unparsed}

/** @author Stephen Samuel */
object CountrySelectOptions {
  def render: Node = Unparsed(Scalate.layout("/com/cloudray/scalapress/util/countries.ssp"))
}
