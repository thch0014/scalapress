package com.liferay.scalapress.media

import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import javax.imageio.ImageIO
import org.springframework.stereotype.Controller
import org.apache.commons.io.IOUtils
import com.liferay.scalapress.Logging
import javax.servlet.http.HttpServletResponse
import java.net.URLConnection
import com.liferay.scalapress.util.mvc.NotFoundException

@Controller
@RequestMapping(Array("images"))
class ImageController extends Logging {

    @Autowired
    var imageProvider: AssetStore = _

    @ExceptionHandler(Array(classOf[NotFoundException]))
    def handleException1(e: NotFoundException, resp: HttpServletResponse) {
        resp.setStatus(404)
    }

    @RequestMapping(value = Array("{filename}"), produces = Array("image/png"), params = Array("width", "height"))
    def imageResized(@PathVariable("filename") filename: String, @RequestParam("width") width: Int,
                     @RequestParam("height") height: Int, resp: HttpServletResponse) {

        imageProvider.get(filename) match {
            case None => throw new NotFoundException
            case Some(in) =>

                val image: java.awt.Image = ImageIO.read(in)
                val thumbnail = ImageTools.fit(image, (width, height))
                resp.setContentType("image/png")
                ImageIO.write(thumbnail, "PNG", resp.getOutputStream)

        }
    }

    @RequestMapping
    @ResponseBody
    def list: Array[Asset] = imageProvider.list(1000)

    @RequestMapping(value = Array("{filename}"))
    def image(@PathVariable("filename") filename: String, resp: HttpServletResponse) {
        resp.setContentType(URLConnection.guessContentTypeFromName(filename))
        imageProvider.get(filename) match {
            case None =>
                logger.debug("Could not find file {}", filename)
            case Some(in) =>
                IOUtils.copy(in, resp.getOutputStream)
                IOUtils.closeQuietly(in)
        }
    }
}