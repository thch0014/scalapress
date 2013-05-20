package com.liferay.scalapress.util.mvc

import collection.mutable.ListBuffer
import com.liferay.scalapress.ScalapressRequest
import com.liferay.scalapress.theme.Theme

/** @author Stephen Samuel */
class ScalapressPage(val theme: Theme, val req: ScalapressRequest) {

    val _body = new ListBuffer[Any]()
    var _toolbar: Option[String] = None

    def toolbar(toolbar: String) {
        _toolbar = Option(toolbar)
    }

    def body(any: Any) = {
        _body += any
        this
    }
}

object ScalapressPage {
    def apply(theme: Theme, req: ScalapressRequest) = new ScalapressPage(theme, req)
}