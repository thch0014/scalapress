package com.liferay.scalapress.controller.web.image

import org.springframework.web.bind.annotation._
import com.liferay.scalapress.service.image.ImageService
import org.springframework.beans.factory.annotation.Autowired
import javax.imageio.ImageIO
import java.io.ByteArrayOutputStream
import net.sf.ehcache.CacheManager
import net.sf.ehcache.Element
import org.springframework.stereotype.Controller
import org.apache.commons.io.{FilenameUtils, IOUtils}
import com.liferay.scalapress.Logging
import javax.servlet.http.HttpServletResponse
import java.net.URLConnection
import com.liferay.scalapress.service.asset.AssetStore
import com.liferay.scalapress.service.asset.Asset
import scala.Some

/**
 * some documentation about this class
 */
@Controller
@RequestMapping(Array("images"))
class ImageController extends Logging {

    @Autowired
    var imageService: ImageService = _

    @Autowired
    var imageProvider: AssetStore = _

    val cacheManager = CacheManager.getInstance()
    cacheManager.addCache("images")
    val cache = cacheManager.getCache("images")

    @ExceptionHandler(Array(classOf[RuntimeException]))
    def handleException1(e: RuntimeException, resp: HttpServletResponse) {
        resp.setStatus(404)
    }

    @RequestMapping(value = Array("{filename}"), produces = Array("image/png"), params = Array("width", "height"))
    def imageResized(@PathVariable("filename") filename: String, @RequestParam("width") width: Int,
                     @RequestParam("height") height: Int, resp: HttpServletResponse) {

        val bytes = Option(cache.get(CacheElement(filename, width, height))) match {
            case None => {
                imageProvider.get(filename) match {
                    case None => throw new RuntimeException
                    case Some(in) =>
                        val image = ImageIO.read(in)
                        val resize = imageService.resize(image, width, height)
                        val ba = new ByteArrayOutputStream()
                        ImageIO.write(resize, "PNG", ba)
                        val array = ba.toByteArray

                        cache.put(new Element(CacheElement(filename, width, height), array))
                        array
                }
            }
            case Some(e) => e.getValue.asInstanceOf[Array[Byte]]
        }
        resp.setContentType(contentType(filename))
        IOUtils.write(bytes, resp.getOutputStream)
    }

    private def contentType(filename: String) = {
        val ext = FilenameUtils.getExtension(filename)
        ext match {
            case "css" => "text/css"
            case "js" => "text/javascript"
            case "gif" => "image/gif"
            case "jpg" => "image/jpg"
            case "jpeg" => "image/jpeg"
            case "png" => "image/png"
            case _ => URLConnection.guessContentTypeFromName(filename)
        }
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