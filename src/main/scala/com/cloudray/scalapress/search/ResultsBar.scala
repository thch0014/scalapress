package com.cloudray.scalapress.search

import com.cloudray.scalapress.framework.ScalapressRequest
import scala.xml.Node
import com.cloudray.scalapress.util.{PagedResult, UrlParser}

/** @author Stephen Samuel */
object ResultsBar {

  val sortsMap = Map(
    "Name" -> Sort.Name,
    "Newest" -> Sort.Newest,
    "Price - Lowest" -> Sort.Price,
    "Price - Highest" -> Sort.PriceHigh
  )

  def render(result: SearchResult, sreq: ScalapressRequest): Node = {
    <div class="search-results-bar">
      {sort(sreq)}{resultCount(result)}
    </div>
  }

  def resultCount(result: SearchResult): Node = {
    val pagedResults = PagedResult(result.refs, result.count)
    <div class="search-result-count" style="float:float">
      {pagedResults.page.first}
      -
      {pagedResults.page.last}
      from
      {result.count}
      Results
    </div>
  }

  def sort(sreq: ScalapressRequest): Node = {
    val url = UrlParser(sreq)
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

