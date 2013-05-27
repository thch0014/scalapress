package com.liferay.scalapress.util.mvc

import collection.mutable.ListBuffer
import com.liferay.scalapress.ScalapressRequest
import com.liferay.scalapress.theme.Theme

/** @author Stephen Samuel */
class ScalapressPage(val theme: Theme, val req: ScalapressRequest) {

    val _body = new ListBuffer[Any]()
    var _toolbar: Option[Toolbar] = None

    def toolbar(sreq: ScalapressRequest) {
        _toolbar = Option(Toolbar(sreq))
    }

    def body(any: Any) = {
        _body += any
        this
    }
}

object ScalapressPage {
    def apply(theme: Theme, req: ScalapressRequest) = new ScalapressPage(theme, req)
}