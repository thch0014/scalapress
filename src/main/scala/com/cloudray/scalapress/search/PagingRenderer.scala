package com.cloudray.scalapress.search

import com.sksamuel.scoot.soa.Paging
import scala.xml.{Node, Utility}
import com.cloudray.scalapress.util.{UrlParser, PageUrlUtils, Pages}
import com.github.theon.uri.Uri
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.framework.ScalapressRequest

/** @author Stephen Samuel */
object PagingRenderer {

  def render(pages: Pages, k: Int, sreq: ScalapressRequest): Node = render(pages, k, sreq.request)
  def render(pages: Pages, k: Int, req: HttpServletRequest): Node = render(pages, k, UrlParser(req))
  def render(pages: Pages, k: Int, uri: Uri): Node = {

    val lis = pages.range(k).map(page => {
      val url = PageUrlUtils.append(uri, page)
      val css = if (page.pageNumber == pages.current.pageNumber) "active" else ""
      <li class={css}>
        <a href={url}>
          {page.pageNumber}
        </a>
      </li>
    })

    <div class="pagination">
      <ul>
        {lis}
      </ul>
    </div>
  }

  @deprecated
  def _renderPages(paging: Paging, range: Int) = {
    paging.range(range).map(p => {
      val url = paging.url(p)
      val css = if (p == paging.pageNumber) "active" else ""
      <li class={css}>
        <a href={url}>
          {p}
        </a>
      </li>
    })
  }

  @deprecated
  def render(paging: Paging): String = render(paging, 5)

  @deprecated
  def render(paging: Paging, range: Int): String = {

    val range = 5
    val pages = _renderPages(paging, range)
    val xml = <div class="pagination">
      <ul>
        <li class="disabled">
          <i class="icon-search">
            &nbsp;
          </i>
          Viewing results
        </li>{pages}
      </ul>
    </div>
    val trimmed = Utility.trim(xml)
    trimmed.toString()
  }

  def renderBootstrap3(paging: Paging, range: Int): String = {

    val range = 5
    val pages = _renderPages(paging, range)
    val xml = <ul class="pagination">
      {pages}
    </ul>
    val trimmed = Utility.trim(xml)
    trimmed.toString()
  }
}
