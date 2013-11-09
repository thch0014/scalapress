package com.cloudray.scalapress.util

import javax.servlet.http.HttpServletRequest
import com.github.theon.uri.Uri
import com.github.theon.uri.Uri._
import java.net.URLDecoder
import com.cloudray.scalapress.framework.ScalapressRequest

/** @author Stephen Samuel */
object UrlParser {

  def parse(sreq: ScalapressRequest): Uri = parse(sreq.request)
  def parse(req: HttpServletRequest): Uri = {
    var uri = parseUri(req.getRequestURL.toString)
    Option(req.getQueryString).foreach(_.split("&").foreach(param => {
      val kv = URLDecoder.decode(param).split("=")
      if (kv.size == 2)
        uri = uri.param(kv(0) -> kv(1))
    }))
    uri
  }
}
