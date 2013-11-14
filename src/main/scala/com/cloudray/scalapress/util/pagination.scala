package com.cloudray.scalapress.util

import com.fasterxml.jackson.annotation.{JsonIgnore, JsonInclude}
import org.apache.commons.lang.builder.ToStringBuilder
import java.util
import scala.collection.JavaConverters._
import com.cloudray.scalapress.framework.ScalapressRequest
import javax.servlet.http.HttpServletRequest
import com.github.theon.uri.Uri

/** @author Stephen Samuel */
case class Page(pageNumber: Int, pageSize: Int) {
  require(pageNumber > 0)
  def next = copy(pageNumber = pageNumber + 1)
  def previous = if (pageNumber <= 0) this else copy(pageNumber = pageNumber - 1)
  // returns the index of the first result on this page. 0 indexed
  def offset: Int = (pageNumber - 1) * pageSize
  // returns the position of the first result on this page. 1 indexed
  def first: Int = offset + 1
  // returns the position of the last result on this page. 1 indexed
  def last: Int = offset + pageSize
}

object Page {
  final val FirstPage = 1
  final val DefaultPageSize = 20
  def empty = Page(FirstPage, DefaultPageSize)
}

object PageUrlUtils {
  def parse(sreq: ScalapressRequest): Page = parse(sreq.request)
  def parse(req: HttpServletRequest): Page = parse(UrlParser(req))
  def parse(uri: Uri): Page = {

    val pageNumber = try {
      uri.query.params.get("pageNumber").flatMap(_.headOption).map(_.toInt).getOrElse(Page.FirstPage)
    } catch {
      case e: NumberFormatException => Page.FirstPage
    }

    val pageSize = try {
      uri.query.params.get("pageSize").flatMap(_.headOption).map(_.toInt).getOrElse(Page.DefaultPageSize)
    } catch {
      case e: NumberFormatException => Page.DefaultPageSize
    }

    Page(if (pageNumber < 1) 1 else pageNumber, pageSize)
  }
  def append(uri: Uri, page: Page) = {
    uri.replaceParams("pageNumber", page.pageNumber).replaceParams("pageSize", page.pageSize)
  }
}

case class Pages(page: Page, totalPages: Int) {
  def before(max: Int): Range = scala.math.max(1, page.pageNumber - max).to(page.pageNumber - 1)
  def after(max: Int): Range = (page.pageNumber + 1).to(scala.math.min(page.pageNumber + max, totalPages))
  def range(dist: Int): Range = before(dist).start.to(after(dist).end)
  def hasPrevious: Boolean = page.pageNumber > 1
  def hasNext: Boolean = page.pageNumber < totalPages
  def isFirst: Boolean = page.pageNumber == 1
  def isLast: Boolean = page.pageNumber == totalPages
  def previous = page.previous
  def previous(k: Int): Seq[Page] = before(k).map(Page(_, page.pageSize))
  def next = page.next
  def next(k: Int): Seq[Page] = after(k).map(Page(_, page.pageSize))
}

@JsonInclude(JsonInclude.Include.NON_EMPTY)
case class PagedResult[T](results: Seq[T], page: Page, totalResults: Int) {
  @JsonIgnore def java = if (results == null) new util.ArrayList[T]() else results.asJava
  override def toString = ToStringBuilder.reflectionToString(this)
}

object PagedResult {

  val FirstPage = 1
  val DefaultPageSize = 20

  /**
   * Returns an empty Page which pageNumber 1, pageSize 20 and and totalResults 0
   */
  def empty[T]: PagedResult[T] = apply(Nil)

  /**
   * Creates a new Page from a given collection of results when it is assumed that those
   * collection of results is the entire result space. It follows that the pageNumber
   * is set to 1 and the totalResults count and pageSize are set to the size of the results.
   *
   * @param results the set of results that is to be assumed to be all available results
   * @return the new Page of results
   */
  def apply[T](results: Iterable[T]): PagedResult[T] = apply(results, results.size)

  /**
   * Creates a new Page from a given collection of results, when the size of the results
   * is to be taken as the size of the pages.
   *
   * @param results the set of results to include in the page
   * @param totalResults the total number of results
   * @return the new Page of results
   */
  def apply[T](results: Iterable[T], totalResults: Int): PagedResult[T] = {
    PagedResult(results.toSeq, Page(FirstPage, results.size), totalResults)
  }
}

case class PagedRequest(page: Page) {
  def offset: Int = (page.pageNumber - 1) * page.pageSize
}
