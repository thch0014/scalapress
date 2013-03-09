package com.liferay.scalapress

import domain.ecommerce.Basket
import domain.{Attachment, Folder, Obj}
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
class ScalapressRequest(val request: HttpServletRequest) {

    if (request.getAttribute("errors") == null)
        request.setAttribute("errors", scala.collection.mutable.Map.empty)

    var obj: Option[Obj] = None
    var attachment: Option[Attachment] = None
    var folder: Option[Folder] = None
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

    def basket: Basket = Option(request.getAttribute("basket")).map(_.asInstanceOf[Basket]).getOrElse(new Basket)
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

