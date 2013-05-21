package com.liferay.scalapress.media

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, ExceptionHandler, PathVariable, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletResponse
import org.apache.commons.io.IOUtils

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("asset"))
class AssetController {

    @Autowired var assetStore: AssetStore = _

    @ExceptionHandler(Array(classOf[RuntimeException]))
    def handleException1(e: RuntimeException, resp: HttpServletResponse) {
        resp.setStatus(404)
    }

    @RequestMapping(value = Array("{key}"))
    @ResponseBody
    def asset(@PathVariable("key") key: String, resp: HttpServletResponse) {
        assetStore.get(key) match {
            case None => throw new RuntimeException
            case Some(input) =>
                IOUtils.copy(input, resp.getOutputStream)
                IOUtils.closeQuietly(input)
                resp.setHeader("Content-Type", ImageTools.contentType(key))
        }
    }
}
