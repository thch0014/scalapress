package com.cloudray.scalapress

import folder.Folder
import javax.servlet.http.{HttpServletResponse, Cookie, HttpServletRequest}
import obj.Obj
import plugin.ecommerce.domain.{OrderLine, Order, BasketLine, Basket}
import com.sksamuel.scoot.soa.Paging
import com.cloudray.scalapress.search.{SearchResult, CorpusResult}
import com.cloudray.scalapress.plugin.ecommerce.dao.BasketDao
import java.util.UUID
import com.cloudray.scalapress.plugin.ecommerce.ShoppingPluginDao
import com.cloudray.scalapress.obj.attr.Attribute
import scala.collection.mutable.ListBuffer

/** @author Stephen Samuel */
case class ScalapressRequest(request: HttpServletRequest,
                             context: ScalapressContext,
                             title: Option[String] = None,
                             obj: Option[Obj] = None,
                             order: Option[Order] = None,
                             orderLine: Option[OrderLine] = None,
                             folder: Option[Folder] = None,
                             searchResult: Option[SearchResult] = None,
                             corpusResult: Option[CorpusResult] = None,
                             line: Option[BasketLine] = None,
                             location: Option[String] = None,
                             paging: Option[Paging] = None,
                             scripts: ListBuffer[String] = new ListBuffer(),
                             styles: ListBuffer[String] = new ListBuffer(),
                             cookies: ListBuffer[Cookie] = new ListBuffer(),
                             response: Option[HttpServletResponse] = None) {

  val cacheKey = "scalapress.cache"
  private val cache = if (request.getAttribute(cacheKey) == null) {
    val cache = new Cache(context)
    request.setAttribute(cacheKey, cache)
    cache
  } else {
    request.getAttribute(cacheKey).asInstanceOf[Cache]
  }

  def shoppingPlugin = cache.shoppingPlugin
  def installation = cache.installation
  def folderSettings = cache.folderSettings
  def widgets = cache.widgets
  def folders = cache.folders
  def folderRoot = cache.folderRoot
  def folder(id: Long): Folder = cache.folder(id)
  def attribute(id: Long): Attribute = cache.attribute(id)

  def cookie(name: String): Option[Cookie] = request.getCookies.find(_.getName == name)

  case class Cache(context: ScalapressContext) {
    lazy val installation = context.installationDao.get
    val _attributes = scala.collection.mutable.Map.empty[Long, Attribute]
    val _folders = scala.collection.mutable.Map.empty[Long, Folder]
    lazy val shoppingPlugin = context.bean[ShoppingPluginDao].get
    lazy val folderSettings = context.folderSettingsDao.head
    lazy val widgets = context.widgetDao.findAll()
    lazy val folders = {
      val all = context.folderDao.findAll()
      all.foreach(folder => _folders.put(folder.id, folder))
      all
    }
    def folder(id: Long): Folder = _folders.get(id).getOrElse({
      val folder = context.folderDao.find(id)
      if (folder != null)
        _folders.put(folder.id, folder)
      folder
    })
    def attribute(id: Long): Attribute = _attributes.get(id).getOrElse({
      val attr = context.attributeDao.find(id)
      if (attr != null)
        _attributes.put(attr.id, attr)
      attr
    })
    lazy val folderRoot = context.folderDao.root
  }

  val errors = if (request.getAttribute("errors") == null) {
    val e = scala.collection.mutable.Map.empty[String, String]
    request.setAttribute("errors", e)
    e
  } else {
    request.getAttribute("errors").asInstanceOf[scala.collection.mutable.Map[String, String]]
  }

  def basket: Basket = {
    Option(request.getAttribute(ScalapressConstants.BasketKey)) match {
      case Some(value) => value.asInstanceOf[Basket]
      case None =>
        val sessionId = request.getAttribute(ScalapressConstants.SessionIdKey).asInstanceOf[String]
        val basket = Option(context.bean[BasketDao].find(sessionId)) match {
          case None =>
            val b = new Basket
            b.sessionId = sessionId
            context.bean[BasketDao].save(b)
            b
          case Some(b) => b
        }
        request.setAttribute(ScalapressConstants.BasketKey, basket)
        basket
    }
  }

  def param(key: String): Option[String] = Option(request.getParameter(key))

  def error(key: String) = errors.get(key)
  def error(key: String, value: String) {
    errors.put(key, value)
  }
  def hasErrors = !errors.isEmpty
  def hasError(key: String) = errors.contains(key)

  def sessionId = Option(request.getAttribute(ScalapressConstants.SessionIdKey)).getOrElse(UUID.randomUUID).toString

  def withPaging(paging: Paging): ScalapressRequest = copy(paging = Option(paging))
  def withLocation(location: String): ScalapressRequest = copy(location = Option(location))
  def withTitle(title: String): ScalapressRequest = copy(title = Option(title))
  def withLine(line: BasketLine): ScalapressRequest = copy(line = Option(line))
  def withFolder(f: Folder): ScalapressRequest = copy(folder = Option(f))
  def withObject(o: Obj): ScalapressRequest = copy(obj = Option(o))
  def withOrder(o: Order): ScalapressRequest = copy(order = Option(o))
  def withOrderLine(o: OrderLine): ScalapressRequest = copy(orderLine = Option(o))
  def withResult(r: CorpusResult): ScalapressRequest = copy(corpusResult = Option(r))
  def withSearchResult(r: SearchResult): ScalapressRequest = copy(searchResult = Option(r))

  def script(js: String): ScalapressRequest = {
    scripts.append(js)
    this
  }

  def style(css: String): ScalapressRequest = {
    styles.append(css)
    this
  }
}

object ScalapressRequest {

  def apply(request: HttpServletRequest, context: ScalapressContext): ScalapressRequest =
    new ScalapressRequest(request, context)
  def apply(obj: Obj, request: HttpServletRequest, context: ScalapressContext): ScalapressRequest =
    ScalapressRequest(request, context).withObject(obj)
  def apply(folder: Folder, request: HttpServletRequest, context: ScalapressContext): ScalapressRequest =
    ScalapressRequest(request, context).withFolder(folder)

}

