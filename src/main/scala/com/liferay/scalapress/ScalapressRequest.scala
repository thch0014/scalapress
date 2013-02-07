package com.liferay.scalapress

import controller.web.ScalapressConstants
import domain.{Attachment, Folder, Obj}
import javax.servlet.http.HttpServletRequest
import plugin.ecommerce.domain.Basket

/** @author Stephen Samuel */
class ScalapressRequest(val request: HttpServletRequest) {

    if (request.getAttribute("errors") == null)
        request.setAttribute("errors", scala.collection.mutable.Map.empty)

    var obj: Option[Obj] = None
    var attachment: Option[Attachment] = None
    var folder: Option[Folder] = None

    def basket: Option[Basket] = Option(request.getAttribute(ScalapressConstants.BasketKey)).map(_.asInstanceOf[Basket])

    def errors = request.getAttribute("errors").asInstanceOf[scala.collection.mutable.Map[String, String]]
    def hasErrors = !errors.isEmpty

    def withObject(o: Obj): ScalapressRequest = {
        this.obj = Option(o)
        this
    }

    def withFolder(f: Folder): ScalapressRequest = {
        this.folder = Option(f)
        this
    }

    def error(key: String) = errors.get(key)
    def error(key: String, value: String) {
        errors.put(key, value)
    }

}

object ScalapressRequest {
    def apply(request: HttpServletRequest) = {
        val r = new ScalapressRequest(request)
        r
    }
    def apply(obj: Obj, request: HttpServletRequest) = {
        val r = new ScalapressRequest(request)
        r.obj = Option(obj)
        r
    }
    def apply(folder: Folder, request: HttpServletRequest) = {
        val r = new ScalapressRequest(request)
        r.folder = Option(folder)
        r
    }
}

