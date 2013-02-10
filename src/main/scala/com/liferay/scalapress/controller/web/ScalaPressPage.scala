package com.liferay.scalapress.controller.web

import collection.mutable.ListBuffer
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.domain.setup.Theme

/** @author Stephen Samuel */
class ScalaPressPage(val theme: Theme, val req: ScalapressRequest) {

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

object ScalaPressPage {
    def apply(theme: Theme, req: HttpServletRequest, context: ScalapressContext) =
        new ScalaPressPage(theme, ScalapressRequest(req, context))
    def apply(theme: Theme, req: ScalapressRequest) = new ScalaPressPage(theme, req)
}