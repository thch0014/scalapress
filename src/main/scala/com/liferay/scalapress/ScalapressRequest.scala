package com.liferay.scalapress

import controller.web.ScalapressConstants
import domain.{Folder, Obj}
import javax.servlet.http.HttpServletRequest
import plugin.ecommerce.domain.{OrderLine, Order, BasketLine, Basket}

/** @author Stephen Samuel */
case class ScalapressRequest(request: HttpServletRequest,
                             context: ScalapressContext,
                             title: Option[String] = None,
                             obj: Option[Obj] = None,
                             order: Option[Order] = None,
                             orderLine: Option[OrderLine] = None,
                             folder: Option[Folder] = None,
                             line: Option[BasketLine] = None) {

    if (request.getAttribute("errors") == null)
        request.setAttribute("errors", scala.collection.mutable.Map.empty)

    def basket: Basket = {
        Option(request.getAttribute(ScalapressConstants.BasketKey)) match {
            case Some(basket) => basket.asInstanceOf[Basket]
            case None =>
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
    }

    def errors = request.getAttribute("errors").asInstanceOf[scala.collection.mutable.Map[String, String]]
    def error(key: String) = errors.get(key)
    def error(key: String, value: String) {
        errors.put(key, value)
    }
    def hasErrors = !errors.isEmpty
    def hasError(key: String) = errors.contains(key)

    def withTitle(title: String): ScalapressRequest = copy(title = Option(title))
    def withLine(line: BasketLine): ScalapressRequest = copy(line = Option(line))
    def withFolder(f: Folder): ScalapressRequest = copy(folder = Option(f))
    def withObject(o: Obj): ScalapressRequest = copy(obj = Option(o))
    def withOrder(o: Order): ScalapressRequest = copy(order = Option(o))
    def withOrderLine(o: OrderLine): ScalapressRequest = copy(orderLine = Option(o))
}

object ScalapressRequest {

    def apply(request: HttpServletRequest, context: ScalapressContext): ScalapressRequest =
        new ScalapressRequest(request, context)
    def apply(obj: Obj, request: HttpServletRequest, context: ScalapressContext): ScalapressRequest =
        ScalapressRequest(request, context).withObject(obj)
    def apply(folder: Folder, request: HttpServletRequest, context: ScalapressContext): ScalapressRequest =
        ScalapressRequest(request, context).withFolder(folder)

}

