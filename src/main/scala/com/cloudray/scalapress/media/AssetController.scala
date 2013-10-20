package com.cloudray.scalapress.media

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, ExceptionHandler, PathVariable, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletResponse
import org.apache.commons.io.IOUtils

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("asset"))
@Autowired
class AssetController(assetStore: AssetStore) {

  @ExceptionHandler(Array(classOf[RuntimeException]))
  def handleException1(e: RuntimeException, resp: HttpServletResponse) {
    resp.setStatus(404)
  }

  @RequestMapping(value = Array("{key}"))
  @ResponseBody
  def asset(@PathVariable("key") key: String, resp: HttpServletResponse) {
    assetStore.get(key) match {
      case None => throw new RuntimeException
      case Some(input) =>
        resp.setContentType(MimeTools.contentType(key))
        IOUtils.copy(input, resp.getOutputStream)
        IOUtils.closeQuietly(input)
    }
  }
}
