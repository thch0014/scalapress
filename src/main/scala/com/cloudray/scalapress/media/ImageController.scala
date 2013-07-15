package com.cloudray.scalapress.media

import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.apache.commons.io.IOUtils
import com.cloudray.scalapress.Logging
import javax.servlet.http.HttpServletResponse
import java.net.URLConnection
import com.sksamuel.scrimage.Format

@Controller
@RequestMapping(Array("images"))
class ImageController extends Logging {

  @Autowired var assetStore: AssetStore = _
  @Autowired var thumbnailService: ThumbnailService = _

  @ResponseBody
  @RequestMapping(value = Array("{w:\\d+}/{h:\\d+}/{filename}"))
  def imageResized3(@PathVariable("filename") filename: String,
                    @PathVariable("w") width: Int,
                    @PathVariable("h") height: Int,
                    resp: HttpServletResponse) {
    imageResized(filename, width, height, resp)
  }

  @ResponseBody
  @RequestMapping(value = Array("{filename}"), params = Array("w", "h"))
  def imageResized2(@PathVariable("filename") filename: String,
                    @RequestParam("w") width: Int,
                    @RequestParam("h") height: Int,
                    resp: HttpServletResponse) {
    imageResized(filename, width, height, resp)
  }

  @ResponseBody
  @RequestMapping(value = Array("{filename}"), params = Array("width", "height"))
  def imageResized(@PathVariable("filename") filename: String,
                   @RequestParam("width") width: Int,
                   @RequestParam("height") height: Int,
                   resp: HttpServletResponse) {

    thumbnailService.thumbnail(filename, width, height) match {
      case None => resp.setStatus(404)
      case Some(thumb) =>
        resp.setContentType("image/png")
        thumb.write(resp.getOutputStream, Format.PNG)
    }
  }

  @RequestMapping(value = Array("{filename}"))
  @ResponseBody
  def image(@PathVariable("filename") filename: String, resp: HttpServletResponse) {
    resp.setContentType(URLConnection.guessContentTypeFromName(filename))
    assetStore.get(filename) match {
      case Some(in) =>
        IOUtils.copy(in, resp.getOutputStream)
        IOUtils.closeQuietly(in)
      case _ =>
        logger.debug("Could not find file {}", filename)
        resp.setStatus(404)
    }
  }
}