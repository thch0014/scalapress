package com.cloudray.scalapress.util.mvc

import collection.mutable.ListBuffer
import com.cloudray.scalapress.theme.Theme
import com.cloudray.scalapress.framework.ScalapressRequest

/** @author Stephen Samuel */
class ScalapressPage(val theme: Theme, val sreq: ScalapressRequest) {

  val _body = new ListBuffer[Any]()
  var _toolbar: Option[Toolbar] = None

  def toolbar(sreq: ScalapressRequest): ScalapressPage = {
    _toolbar = Option(Toolbar(sreq))
    this
  }

  def body(any: Any): ScalapressPage = {
    _body += any
    this
  }

  def render = _body.filter(_ != null).map(_.toString).mkString("\n\n")
}

object ScalapressPage {
  def apply(theme: Theme, req: ScalapressRequest) = new ScalapressPage(theme, req)
}