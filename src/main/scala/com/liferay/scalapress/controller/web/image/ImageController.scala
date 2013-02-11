package com.liferay.scalapress.controller.web.image

import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import javax.imageio.ImageIO
import org.springframework.stereotype.Controller
import org.apache.commons.io.IOUtils
import com.liferay.scalapress.Logging
import javax.servlet.http.HttpServletResponse
import java.net.{URL, URLConnection}
import com.liferay.scalapress.service.asset.AssetStore
import com.liferay.scalapress.service.asset.Asset
import scala.Some
import com.liferay.scalapress.service.image.ImageService

@Controller
@RequestMapping(Array("images"))
class ImageController extends Logging {

    @Autowired
    var imageProvider: AssetStore = _

    //    val cacheManager = CacheManager.getInstance()
    //    cacheManager.addCache("images")
    //    val cache = cacheManager.getCache("images")

    @ExceptionHandler(Array(classOf[RuntimeException]))
    def handleException1(e: RuntimeException, resp: HttpServletResponse) {
        resp.setStatus(404)
    }

    @RequestMapping(value = Array("test"), produces = Array("image/png"), params = Array("width", "height"))
    def test(@RequestParam("width") width: Int,
             @RequestParam("height") height: Int,
             resp: HttpServletResponse) {

        val url = new URL("http://childrenstorytales.com/wp-content/uploads/2011/08/large-siberian-tiger1-1024x768.jpg")

        val image = ImageIO.read(url.openStream())
        val thumbnail = ImageService.fit(image, (width, height))

        ImageIO.write(thumbnail, "PNG", resp.getOutputStream)
        resp.setContentType(ImageService.contentType("image/png"))
    }

    @RequestMapping(value = Array("{filename}"), produces = Array("image/png"), params = Array("width", "height"))
    def imageResized(@PathVariable("filename") filename: String, @RequestParam("width") width: Int,
                     @RequestParam("height") height: Int, resp: HttpServletResponse) {

        //    val bytes = Option(cache.get(CacheElement(filename, width, height))) match {
        //       case None => {
        imageProvider.get(filename) match {
            case None => throw new RuntimeException
            case Some(in) =>

                val image = ImageIO.read(in)
                val thumbnail = ImageService.fit(image, (width, height))
                ImageIO.write(thumbnail, "PNG", resp.getOutputStream)
                resp.setContentType("image/png")

            //          cache.put(new Element(CacheElement(filename, width, height), array))

        }
        //       }
        //       case Some(e) => e.getObjectValue.asInstanceOf[Array[Byte]]
        //   }
    }

    @RequestMapping
    @ResponseBody
    def list: Array[Asset] = imageProvider.list(1000)

    @RequestMapping(value = Array("{filename}"))
    def image(@PathVariable("filename") filename: String, resp: HttpServletResponse) {
        resp.setContentType(URLConnection.guessContentTypeFromName(filename))
        imageProvider.get(filename) match {
            case None => {
                logger.debug("Could not find file {}", filename)
            }
            case Some(in) => IOUtils.copy(in, resp.getOutputStream)
        }
    }
}

case class CacheElement(filename: String, w: Int, h: Int)