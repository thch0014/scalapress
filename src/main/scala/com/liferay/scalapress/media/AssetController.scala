package com.liferay.scalapress.media

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, ExceptionHandler, PathVariable, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletResponse
import com.liferay.scalapress.service.asset.AssetStore

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("asset"))
class AssetController {

    @Autowired var assetStore: AssetStore = _

    @ExceptionHandler(Array(classOf[RuntimeException]))
    def handleException1(e: RuntimeException, resp: HttpServletResponse) {
        resp.setStatus(404)
    }

    @RequestMapping(value = Array("{filename}"))
    @ResponseBody
    def asset(@PathVariable("filename") filename: String, resp: HttpServletResponse) {
        resp.sendRedirect(assetStore.link(filename))
    }
}
