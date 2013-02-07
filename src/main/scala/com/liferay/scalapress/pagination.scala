package com.liferay.scalapress

import reflect.BeanProperty
import org.apache.commons.lang.builder.ToStringBuilder
import javax.servlet.http.HttpServletRequest
import org.codehaus.jackson.annotate.JsonIgnore

/** @author Stephen Samuel */
class PagedQuery {
    @BeanProperty var pageNumber: Int = Page.FirstPage
    @BeanProperty var pageSize: Int = Page.DefaultPageSize
    def offset: Int = (pageNumber - 1) * pageSize
}

import scala.collection.JavaConverters._

class Page[T] {

    @BeanProperty var results: List[T] = _
    @BeanProperty var pageNumber: Int = _
    @BeanProperty var pageSize: Int = _
    @BeanProperty var totalResults: Int = _
    @BeanProperty var numberOfResults: Int = _

    @JsonIgnore def java = results.asJava
    override def toString = ToStringBuilder.reflectionToString(this)
}

object Page {

    val FirstPage = 1
    val DefaultPageSize = 20

    def empty[T]: Page[T] = apply(Nil, FirstPage, DefaultPageSize, 0)
    def apply[T](iterable: java.lang.Iterable[T]): Page[T] = apply(iterable, FirstPage)

    def apply[T](iterable: java.lang.Iterable[T], totalResults: Int): Page[T] =
        apply(iterable, FirstPage, totalResults, totalResults)

    def apply[T](iterable: java.lang.Iterable[T], pageNumber: Int, pageSize: Int, totalResults: Int): Page[T] =
        apply(iterable.asScala.toList, pageNumber, pageSize, totalResults)

    def apply[T](iterable: java.lang.Iterable[T], req: PagedQuery, totalResults: Int): Page[T] =
        apply(iterable, req.pageNumber, req.pageSize, totalResults)

    def apply[T](array: Array[T]): Page[T] = apply(array, FirstPage)

    def apply[T](array: Array[T], totalResults: Int): Page[T] = apply(array.toList, 1, totalResults, totalResults)

    /**
     * Returns a Page that wraps the given iterable, where the page size
     * and total results is taken as the iterable size
     */
    def apply[T](iterable: Iterable[T]): Page[T] = {
        val list = iterable.toList
        val size = list.size
        apply(list, 1, size, size)
    }

    /**
     * Returns a Page that wraps the given iterable, where the page size is the size of the iterable
     * and the total results is as given
     */
    def apply[T](iterable: Iterable[T], totalResults: Int): Page[T] =
        apply(iterable.toList, 1, totalResults, totalResults)

    def apply[T](list: List[T], pageNumber: Int, pageSize: Int, totalResults: Int): Page[T] = {
        val page = new Page[T]
        page.results = list
        page.pageNumber = pageNumber
        page.pageSize = pageSize
        page.totalResults = totalResults
        page.numberOfResults = list.size
        page
    }
}

class Paging(url: UriBuilder, @BeanProperty val pageNumber: Int, @BeanProperty val totalPages: Int) {
    def before(max: Int): Array[Int] = scala.math.max(1, pageNumber - max).to(pageNumber - 1).toArray
    def after(max: Int): Array[Int] = (pageNumber + 1).to(scala.math.min(pageNumber + max, totalPages)).toArray
    def previous: Int = pageNumber - 1
    def next: Int = pageNumber + 1
    def hasPreviousPage: Boolean = pageNumber > 1
    def hasNextPage: Boolean = pageNumber < totalPages
    def isFirstPage: Boolean = pageNumber == 1
    def isLastPage: Boolean = pageNumber == totalPages
    def url(pageNumber: Int): String = (url ? ("pageNumber" -> pageNumber)).toString
    override def toString = ToStringBuilder.reflectionToString(this)
}

object Paging {
    def apply(req: HttpServletRequest, page: Page[_]): Paging =
        apply(req.getRequestURL.append("?").append(Option(req.getQueryString).getOrElse("")).toString, page)
    def apply(url: String, page: Page[_]): Paging = {
        assert(page.pageNumber > 0)
        val totalPages = if (page.pageSize == 0) 0 else scala.math.ceil(page.totalResults / page.pageSize).toInt
        new Paging(UriBuilder(url), page.pageNumber, totalPages)
    }
}