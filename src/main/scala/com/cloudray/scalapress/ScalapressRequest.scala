package com.cloudray.scalapress

import folder.Folder
import javax.servlet.http.HttpServletRequest
import obj.Obj
import plugin.ecommerce.domain.{OrderLine, Order, BasketLine, Basket}
import com.sksamuel.scoot.soa.Paging

/** @author Stephen Samuel */
case class ScalapressRequest(request: HttpServletRequest,
                             context: ScalapressContext,
                             title: Option[String] = None,
                             obj: Option[Obj] = None,
                             order: Option[Order] = None,
                             orderLine: Option[OrderLine] = None,
                             folder: Option[Folder] = None,
                             line: Option[BasketLine] = None,
                             location: Option[String] = None,
                             paging: Option[Paging] = None) {

    val errors = if (request.getAttribute("errors") == null) {
        val e = scala.collection.mutable.Map.empty[String, String]
        request.setAttribute("errors", e)
        e
    } else {
        request.getAttribute("errors").asInstanceOf[scala.collection.mutable.Map[String, String]]
    }

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

    lazy val installation = context.installationDao.get
    def folders = context.folderDao.findAll()
    lazy val folderRoot = context.folderDao.root
    lazy val widgets = context.widgetDao.findAll()

    def param(key: String): Option[String] = Option(request.getParameter(key))

    def error(key: String) = errors.get(key)
    def error(key: String, value: String) {
        errors.put(key, value)
    }
    def hasErrors = !errors.isEmpty
    def hasError(key: String) = errors.contains(key)

    def sessionId = request.getAttribute(ScalapressConstants.SessionIdKey).toString

    def withPaging(paging: Paging): ScalapressRequest = copy(paging = Option(paging))
    def withLocation(location: String): ScalapressRequest = copy(location = Option(location))
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
