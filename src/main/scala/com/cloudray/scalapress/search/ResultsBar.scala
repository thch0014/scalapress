package com.cloudray.scalapress.search

import com.cloudray.scalapress.framework.ScalapressRequest
import scala.xml.Node

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
      {resultCount(result)}{sort(sreq)}
    </div>
  }

  def resultCount(result: SearchResult): Node = {
    <div class='search-result-count'>
      {result.count}
      Results
    </div>
  }

  def sort(sreq: ScalapressRequest): Node = {

    val sort = SearchUrlUtils.sort(sreq)
    val options = ResultsBar.sortsMap.map(entry => {
      val selected = entry._2 == sort
      <option value={entry._2.name} selected={selected.toString}>
        {entry._1}
      </option>
    })

    <div class='search-result-sort'>Sort by:
      <select name="sort">
        {options}
      </select>
    </div>
  }
}

