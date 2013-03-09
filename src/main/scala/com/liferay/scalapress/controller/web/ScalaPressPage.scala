package com.liferay.scalapress.controller.web

import collection.mutable.ListBuffer
import com.liferay.scalapress.ScalapressRequest
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.domain.setup.Theme

/** @author Stephen Samuel */
class ScalaPressPage(val theme: Theme, val req: ScalapressRequest) {

    val _body = new ListBuffer[Any]()

    def body(any: Any) = {
        _body += any
        this
    }
}

object ScalaPressPage {
    def apply(theme: Theme, req: HttpServletRequest) = new ScalaPressPage(theme, ScalapressRequest(req))
    def apply(theme: Theme, req: ScalapressRequest) = new ScalaPressPage(theme, req)
}