package com.cloudray.scalapress.media

import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import javax.imageio.ImageIO
import org.springframework.stereotype.Controller
import org.apache.commons.io.IOUtils
import com.cloudray.scalapress.Logging
import javax.servlet.http.HttpServletResponse
import java.net.URLConnection
import java.io.ByteArrayInputStream

@Controller
@RequestMapping(Array("images"))
class ImageController extends Logging {

    @Autowired
    var assetStore: AssetStore = _

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

        assetStore.get(filename) match {
            case Some(in) =>

                val bytes = IOUtils.toByteArray(in)
                IOUtils.closeQuietly(in)

                bytes.length match {
                    case 0 =>
                        logger.debug("Image asset has 0 bytes [{}]", filename)
                        resp.setStatus(404)
                    case _ =>
                        val image = ImageIO.read(new ByteArrayInputStream(bytes))
                        Option(image) match {
                            case None =>
                                logger.debug("Image could not be decoded by ImageIO [{}]", filename)
                                resp.setStatus(404)
                            case Some(i) =>
                                logger.debug("Sizing image {}", filename)
                                val thumbnail = ImageTools.fit(image, (width, height))
                                resp.setContentType("image/png")
                                ImageIO.write(thumbnail, "PNG", resp.getOutputStream)
                        }
                }
            case _ =>
                logger.debug("Could not find file {}", filename)
                resp.setStatus(404)
        }
    }

    @RequestMapping
    @ResponseBody
    def list: Array[Asset] = assetStore.list(1000)

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