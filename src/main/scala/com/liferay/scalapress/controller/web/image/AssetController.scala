package com.liferay.scalapress.controller.web.image

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import org.apache.commons.io.{FilenameUtils, IOUtils}
import javax.servlet.http.HttpServletResponse
import java.net.URLConnection
import com.liferay.scalapress.service.asset.AssetStore

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("asset"))
class AssetController {

    @Autowired var assetStore: AssetStore = _

    @ResponseBody
    @RequestMapping(value = Array("{filename}"))
    def image(@PathVariable("filename") filename: String, resp: HttpServletResponse): Array[Byte] = {
        assetStore.get(filename) match {
            case None => throw new RuntimeException(filename + " not found")
            case Some(in) => {
                val bytes = IOUtils.toByteArray(in)
                resp.setContentType(contentType(filename))
                bytes
            }
        }
    }

    private def contentType(filename: String) = FilenameUtils.getExtension(filename) match {
        case "css" => "text/css"
        case "js" => "text/javascript"
        case _ => URLConnection.guessContentTypeFromName(filename)
    }
}
