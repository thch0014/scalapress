package com.cloudray.scalapress.media

import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.apache.commons.io.IOUtils
import javax.servlet.http.HttpServletResponse
import java.net.URLConnection
import com.sksamuel.scrimage.Format
import com.cloudray.scalapress.framework.Logging

@Controller
@Autowired
@RequestMapping(Array("images"))
class ImageController(assetStore: AssetStore,
                      thumbnailService: ThumbnailService) extends Logging with OpType {

  @ResponseBody
  @RequestMapping(value = Array("{w:\\d+}/{h:\\d+}/{filename}"))
  @deprecated
  def imageResized3(@PathVariable("filename") filename: String,
                    @PathVariable("w") width: Int,
                    @PathVariable("h") height: Int,
                    resp: HttpServletResponse) {
    imageResized(filename, width, height, "fit", resp)
  }

  @ResponseBody
  @RequestMapping(value = Array("{filename}"), params = Array("w", "h"))
  @deprecated
  def imageResized2(@PathVariable("filename") filename: String,
                    @RequestParam("w") width: Int,
                    @RequestParam("h") height: Int,
                    @RequestParam(value = "type", required = false) `type`: String,
                    resp: HttpServletResponse) {
    imageResized(filename, width, height, `type`, resp)
  }

  @ResponseBody
  @RequestMapping(value = Array("{filename}"), params = Array("width", "height"))
  @deprecated
  def imageResized(@PathVariable("filename") filename: String,
                   @RequestParam("width") width: Int,
                   @RequestParam("height") height: Int,
                   @RequestParam(value = "type", required = false) `type`: String,
                   resp: HttpServletResponse) {

    thumbnailService.thumbnail(filename, width, height, _opType(Option(`type`))) match {
      case Some(thumb) =>
        resp.setContentType("image/png")
        thumb.write(resp.getOutputStream, Format.PNG)
      case _ => resp.setStatus(404)
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