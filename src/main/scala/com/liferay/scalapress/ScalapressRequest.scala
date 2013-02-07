package com.liferay.scalapress

import domain.{Attachment, Folder, Obj}
import javax.servlet.http.HttpServletRequest
import plugin.ecommerce.domain.Basket

/** @author Stephen Samuel */
class ScalapressRequest(val request: HttpServletRequest) {

    if (request.getAttribute("errors") == null)
        request.setAttribute("errors", scala.collection.mutable.Map.empty)

    var title: Option[String] = None
    var obj: Option[Obj] = None
    var attachment: Option[Attachment] = None
    var folder: Option[Folder] = None

    def basket: Option[Basket] = {
//        Option(request.getAttribute(ScalapressConstants.SessionIdKey)).map(_.asInstanceOf[String]).map(sessionId => {
        //            val basket = basketDao.find(sessionId)
        //            request.setAttribute(ScalapressConstants.BasketKey, basket)
        //        })
        None
    }

    def errors = request.getAttribute("errors").asInstanceOf[scala.collection.mutable.Map[String, String]]
    def hasErrors = !errors.isEmpty

    def withTitle(title: String): ScalapressRequest = {
        this.title = Option(title)
        this
    }

    def withObject(o: Obj): ScalapressRequest = {
        this.obj = Option(o)
        this.title = obj.map(_.name)
        this
    }

    def withFolder(f: Folder): ScalapressRequest = {
        this.folder = Option(f)
        this.title = folder.map(_.name)
        this
    }

    def error(key: String) = errors.get(key)
    def error(key: String, value: String) {
        errors.put(key, value)
    }

}

object ScalapressRequest {

    def apply(request: HttpServletRequest) = new ScalapressRequest(request)
    def apply(obj: Obj, request: HttpServletRequest) = new ScalapressRequest(request).withObject(obj)
    def apply(folder: Folder, request: HttpServletRequest) = new ScalapressRequest(request).withFolder(folder)

}

