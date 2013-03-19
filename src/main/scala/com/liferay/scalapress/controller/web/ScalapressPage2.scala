package com.liferay.scalapress.controller.web

import collection.mutable.ListBuffer
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.domain.setup.Theme

/** @author Stephen Samuel */
class ScalapressPage2(val theme: Theme, val req: ScalapressRequest) {

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

object ScalapressPage2 {

    @deprecated
    def apply(theme: Theme, req: HttpServletRequest, context: ScalapressContext) =
        new ScalapressPage2(theme, ScalapressRequest(req, context))

    def apply(theme: Theme, req: ScalapressRequest) = new ScalapressPage2(theme, req)
}