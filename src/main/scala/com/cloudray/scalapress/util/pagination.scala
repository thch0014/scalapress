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
  def next = copy(pageNumber = pageNumber + 1)
  def previous = if (pageNumber <= 0) this else copy(pageNumber = pageNumber - 1)
  // returns the index for first element on this page starting at 0
  def offset: Int = (pageNumber - 1) * pageSize
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

    Page(pageNumber, pageSize)
  }
  def append(uri: Uri, page: Page) = {
    uri.replaceParams("pageNumber", page.pageNumber).replaceParams("pageSize", page.pageSize)
  }
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

