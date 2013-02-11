package com.liferay.scalapress.controller.web.image

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import org.apache.commons.io.IOUtils
import javax.servlet.http.HttpServletResponse
import com.liferay.scalapress.service.asset.AssetStore
import com.liferay.scalapress.service.image.ImageTools

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("asset"))
class AssetController {

    @Autowired var assetStore: AssetStore = _

    @RequestMapping(value = Array("{filename}"))
    def image(@PathVariable("filename") filename: String, resp: HttpServletResponse) {
        assetStore.get(filename) match {
            case None => throw new RuntimeException(filename + " not found")
            case Some(in) => {
                val bytes = IOUtils.toByteArray(in)
                resp.setContentType(ImageTools.contentType(filename))
                IOUtils.write(bytes, resp.getOutputStream)
            }
        }
    }
}
