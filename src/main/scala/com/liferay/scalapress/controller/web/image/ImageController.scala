package com.liferay.scalapress.controller.web.image

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import com.liferay.scalapress.service.image.ImageService
import org.springframework.beans.factory.annotation.{Value, Autowired}
import org.springframework.web.bind.annotation.ResponseBody
import javax.imageio.ImageIO
import org.springframework.web.bind.annotation.RequestParam
import java.io.ByteArrayOutputStream
import net.sf.ehcache.CacheManager
import net.sf.ehcache.Element
import org.springframework.stereotype.Controller
import org.apache.commons.io.IOUtils
import com.liferay.scalapress.Logging
import javax.servlet.http.HttpServletResponse
import java.net.URLConnection
import com.liferay.scalapress.service.asset.{Asset, AssetStore}

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

    @Value("${ecreator.migration.domain}") var migrationDomain: String = _

    @ResponseBody
    @RequestMapping(value = Array("{filename}"), produces = Array("image/png"), params = Array("width", "height"))
    def imageResized(@PathVariable("filename") filename: String, @RequestParam("width") width: Int,
                     @RequestParam("height") height: Int): Array[Byte] = {

        Option(cache.get(CacheElement(filename, width, height))) match {
            case None => {

                val in = imageProvider.get(filename)
                val image = ImageIO.read(in.get)
                val resize = imageService.resize(image, width, height)
                val ba = new ByteArrayOutputStream()
                ImageIO.write(resize, "PNG", ba)
                val array = ba.toByteArray

                cache.put(new Element(CacheElement(filename, width, height), array))
                array
            }
            case Some(e) => e.getValue.asInstanceOf[Array[Byte]]
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