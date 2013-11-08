package com.cloudray.scalapress.search

import com.cloudray.scalapress.framework.ScalapressRequest
import com.github.theon.uri.Uri._
import com.github.theon.uri.Uri
import java.net.URLDecoder

/**
 * Parses the request URL for selected facets
 *
 * @author Stephen Samuel */
object SelectedFacetUrlParser {

  def parse(sreq: ScalapressRequest): Uri = {
    var uri = parseUri(sreq.request.getRequestURL.toString)
    Option(sreq.request.getQueryString).foreach(_.split("&").foreach(param => {
      val kv = URLDecoder.decode(param).split("=")
      if (kv.size == 2)
        uri = uri.param(kv(0) -> kv(1))
    }))
    uri
  }

  def selectedFacets(sreq: ScalapressRequest): Seq[SelectedFacet] = selectedFacets(parse(sreq))
  def selectedFacets(uri: Uri): Seq[SelectedFacet] = {
    uri.query.params
      .map(param => SelectedFacet(FacetField(param._1), param._2.head))
      .filterNot(_ == UnknownFacetField)
      .toSeq
  }
}
