package com.cloudray.scalapress.util.mvc

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import javax.servlet.http.HttpServletResponse
import org.apache.commons.io.IOUtils

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("loginbackground"))
class BackgroundController {

  val IMAGE_COUNT = 31

  @ResponseBody
  @RequestMapping(produces = Array("image/jpeg"))
  def background(resp: HttpServletResponse) {
    val imageNumber = System.currentTimeMillis % IMAGE_COUNT + 1
    val input = getClass.getResourceAsStream("/background/loginbg" + imageNumber + ".jpg")
    IOUtils.copy(input, resp.getOutputStream)
  }
}
