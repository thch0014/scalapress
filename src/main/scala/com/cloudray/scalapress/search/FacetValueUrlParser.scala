package com.cloudray.scalapress.search

import com.cloudray.scalapress.framework.ScalapressRequest
import com.github.theon.uri.Uri
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.util.UrlParser

/**
 * Parses the request URL for facet values
 *
 * @author Stephen Samuel */
object FacetValueUrlParser {

  def parse(sreq: ScalapressRequest): Iterable[FacetValue] = parse(UrlParser.parse(sreq))
  def parse(req: HttpServletRequest): Iterable[FacetValue] = parse(UrlParser.parse(req))
  def parse(uri: Uri): Iterable[FacetValue] = {
    uri.query.params
      .map(param => (FacetField(param._1), param._2.head))
      .filter(_._1.isDefined)
      .map(arg => FacetValue(arg._1.get, arg._2))
  }
}
