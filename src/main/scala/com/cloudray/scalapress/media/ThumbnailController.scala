package com.cloudray.scalapress.media

import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import com.cloudray.scalapress.Logging
import javax.servlet.http.HttpServletResponse
import com.sksamuel.scrimage.Format

@Controller
@RequestMapping(Array("thumbnail"))
class ThumbnailController extends Logging with OpType {

  @Autowired var thumbnailService: ThumbnailService = _

  @ResponseBody
  @RequestMapping(value = Array("{filename}"), params = Array("w", "h", "opType"))
  @deprecated
  def thumbnail(@PathVariable("filename") filename: String,
                @RequestParam("w") width: Int,
                @RequestParam("h") height: Int,
                @RequestParam(value = "opType") opType: String,
                resp: HttpServletResponse) {
    thumbnailService.thumbnail(filename, width, height, _opType(Option(opType))) match {
      case Some(thumb) =>
        resp.setContentType("image/png")
        thumb.write(resp.getOutputStream, Format.PNG)
      case _ => resp.setStatus(404)
    }
  }
}