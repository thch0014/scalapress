package com.liferay.scalapress

import controller.web.ScalapressConstants
import domain.{Attachment, Folder, Obj}
import javax.servlet.http.HttpServletRequest
import plugin.ecommerce.domain.Basket

/** @author Stephen Samuel */
class ScalapressRequest(val request: HttpServletRequest, val context: ScalapressContext) {

    if (request.getAttribute("errors") == null)
        request.setAttribute("errors", scala.collection.mutable.Map.empty)

    var title: Option[String] = None
    var obj: Option[Obj] = None
    var attachment: Option[Attachment] = None
    var folder: Option[Folder] = None

    def basket: Basket = {
        val sessionId = request.getAttribute(ScalapressConstants.SessionIdKey).asInstanceOf[String]
        val basket = Option(context.basketDao.find(sessionId)) match {
            case None =>
                val b = new Basket
                b.sessionId = sessionId
                context.basketDao.save(b)
                b
            case Some(b) => b
        }
        request.setAttribute(ScalapressConstants.BasketKey, basket)
        basket
    }

    def errors = request.getAttribute("errors").asInstanceOf[scala.collection.mutable.Map[String, String]]
    def hasErrors = !errors.isEmpty

    def withTitle(title: String): ScalapressRequest = {
        this.title = Option(title)
        this
    }

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

    def apply(request: HttpServletRequest, context: ScalapressContext) = new ScalapressRequest(request, context)
    def apply(obj: Obj, request: HttpServletRequest, context: ScalapressContext) =
        new ScalapressRequest(request, context).withObject(obj)
    def apply(folder: Folder, request: HttpServletRequest, context: ScalapressContext) =
        new ScalapressRequest(request, context).withFolder(folder)

}

