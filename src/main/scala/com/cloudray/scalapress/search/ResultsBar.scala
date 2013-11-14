package com.cloudray.scalapress.search

import com.cloudray.scalapress.framework.ScalapressRequest
import scala.xml.Node
import com.cloudray.scalapress.util.{PageUrlUtils, PagedResult, UrlParser}
import com.github.theon.uri.Uri
import scala.collection.mutable.ListBuffer

/** @author Stephen Samuel */
object ResultsBar {

  val sortsMap = Map(
    "Name" -> Sort.Name,
    "Newest" -> Sort.Newest,
    "Price - Lowest" -> Sort.Price,
    "Price - Highest" -> Sort.PriceHigh
  )

  def render(result: SearchResult, sreq: ScalapressRequest): Node = {

    val url = UrlParser(sreq)
    val page = PageUrlUtils.parse(url)
    val pagedResult = PagedResult(result.refs, page, result.count)

    val components = new ListBuffer[Node]
    components.append(sort(sreq, url))
    if (pagedResult.pages.multiple) components.append(paging(pagedResult, url))
    components.append(resultCount(pagedResult))

    <div class="search-results-bar">
      {components}
    </div>
  }

  def paging(pagedResult: PagedResult[ItemRef], url: Uri): Node = {
    <div class="search-result-paging" style="float:float">
      {PagingRenderer.render(pagedResult.pages, 4, url)}
    </div>
  }

  def resultCount(pagedResult: PagedResult[ItemRef]): Node = {
    <div class="search-result-count" style="float:float">
      {pagedResult.page.first}
      -
      {pagedResult.page.last}
      from
      {pagedResult.totalResults}
      Results
    </div>
  }

  def sort(sreq: ScalapressRequest, url: Uri): Node = {
    val sort = SearchUrlUtils.sort(sreq)
    val options = ResultsBar.sortsMap.map(entry => {
      val selected = if (entry._2 == sort) "true" else null
      <option value={entry._2.name} selected={selected}>
        {entry._1}
      </option>
    })

    val params = url.query.removeParams("sort").params.flatMap(param => param._2.map(value => {
        <input type="hidden" name={param._1} value={value}/>
    }))
    <div class="search-result-sort" style="float:right">
      <form method="GET" action={url}>
        {params}
        Sort by:
        <select name="sort" onChange="submit()">
          {options}
        </select>
      </form>
    </div>
  }
}

