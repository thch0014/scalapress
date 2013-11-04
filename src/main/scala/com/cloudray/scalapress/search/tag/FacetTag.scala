package com.cloudray.scalapress.search.tag

import com.cloudray.scalapress.theme.tag.ScalapressTag
import java.net.URLDecoder
import com.cloudray.scalapress.search.controller.FacetRenderer
import com.github.theon.uri.Uri._
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("facets")
class FacetTag extends ScalapressTag {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    request.searchResult match {

      case Some(result) if result.facets.size > 0 =>

        var uri = parseUri(request.request.getRequestURL.toString)
        Option(request.request.getQueryString).foreach(_.split("&").foreach(param => {
          val kv = URLDecoder.decode(param).split("=")
          if (kv.size == 2)
            uri = uri.param(kv(0) -> kv(1))
        }))

        val node = FacetRenderer.render(result.facets, uri)
        Some(node.toString())

      case _ => None
    }
  }
}