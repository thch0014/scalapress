package com.cloudray.scalapress.util

import javax.servlet.http.HttpServletRequest
import com.github.theon.uri.Uri
import com.github.theon.uri.Uri._
import java.net.URLDecoder
import com.cloudray.scalapress.framework.ScalapressRequest

/** @author Stephen Samuel */
object UrlParser {

  def apply(sreq: ScalapressRequest): Uri = apply(sreq.request)
  def apply(req: HttpServletRequest): Uri = {
    var uri = parseUri(req.getRequestURL.toString)
    Option(req.getQueryString).foreach(_.split("&").foreach(param => {
      val kv = URLDecoder.decode(param).split("=")
      if (kv.size == 2)
        uri = uri.param(kv(0) -> kv(1))
    }))
    uri
  }
}
