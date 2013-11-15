package com.cloudray.scalapress.framework

import javax.servlet.http.{Cookie, HttpServletRequest}
import com.sksamuel.scoot.soa.Paging
import com.cloudray.scalapress.search.{SearchResult, CorpusResult}
import com.cloudray.scalapress.item.attr.Attribute
import scala.collection.mutable.ListBuffer
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.plugin.ecommerce.domain.{Basket, BasketLine, OrderLine, Order}
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.ShoppingPluginDao
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.BasketDao

/** @author Stephen Samuel */
case class ScalapressRequest(request: HttpServletRequest,
                             context: ScalapressContext,
                             title: Option[String] = None,
                             item: Option[Item] = None,
                             order: Option[Order] = None,
                             orderLine: Option[OrderLine] = None,
                             folder: Option[Folder] = None,
                             searchResult: Option[SearchResult] = None,
                             corpusResult: Option[CorpusResult] = None,
                             line: Option[BasketLine] = None,
                             location: Option[String] = None,
                             paging: Option[Paging] = None,
                             scripts: ListBuffer[String] = new ListBuffer(),
                             styles: ListBuffer[String] = new ListBuffer()) extends Logging {

  if (request.getAttribute("outgoingCookies") == null) {
    request.setAttribute("outgoingCookies", new ListBuffer[Cookie])
  }

  val cacheKey = "scalapress.cache"
  private val cache = if (request.getAttribute(cacheKey) == null) {
    val cache = new Cache(context)
    request.setAttribute(cacheKey, cache)
    cache
  } else {
    request.getAttribute(cacheKey).asInstanceOf[Cache]
  }

  def outgoingCookies: ListBuffer[Cookie] =
    Option(request.getAttribute("outgoingCookies"))
      .map(_.asInstanceOf[ListBuffer[Cookie]])
      .getOrElse(new ListBuffer[Cookie])
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
    lazy val widgets = context.widgetDao.findAll
    lazy val folders = {
      val all = context.folderDao.findAll
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

  val errors = {
    if (request.getAttribute("errors") == null) {
      val e = scala.collection.mutable.Map.empty[String, String]
      request.setAttribute("errors", e)
      e
    } else {
      request.getAttribute("errors").asInstanceOf[scala.collection.mutable.Map[String, String]]
    }
  }

  /**
   * Retrieves a basket for this request.
   * If no basket yet exists, then one will be created and persisted.
   */
  def basket: Basket = {
    // we store the basket in the http request
    Option(request.getAttribute(ScalapressConstants.RequestAttributeKey_Basket)) match {
      case Some(value) => value.asInstanceOf[Basket]
      case None =>
        val basket = context.bean[BasketDao].get(sessionId).getOrElse({
          val b = new Basket
          b.sessionId = sessionId
          context.bean[BasketDao].save(b)
          b
        })
        request.setAttribute(ScalapressConstants.RequestAttributeKey_Basket, basket)
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

  def sessionId: String = {
    request.getAttribute(ScalapressConstants.RequestAttributeKey_SessionId).asInstanceOf[String]
  }

  def withPaging(paging: Paging): ScalapressRequest = copy(paging = Option(paging))
  def withLocation(location: String): ScalapressRequest = copy(location = Option(location))
  def withTitle(title: String): ScalapressRequest = copy(title = Option(title))
  def withLine(line: BasketLine): ScalapressRequest = copy(line = Option(line))
  def withFolder(f: Folder): ScalapressRequest = copy(folder = Option(f))
  def withItem(o: Item): ScalapressRequest = copy(item = Option(o))
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
  def apply(item: Item, request: HttpServletRequest, context: ScalapressContext): ScalapressRequest =
    ScalapressRequest(request, context).withItem(item)
  def apply(folder: Folder, request: HttpServletRequest, context: ScalapressContext): ScalapressRequest =
    ScalapressRequest(request, context).withFolder(folder)

}

