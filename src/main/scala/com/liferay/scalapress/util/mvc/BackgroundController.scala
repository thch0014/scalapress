package com.liferay.scalapress.util.mvc

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import util.Random
import javax.servlet.http.HttpServletResponse
import org.apache.commons.io.IOUtils

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("loginbackground"))
class BackgroundController {

    @ResponseBody
    @RequestMapping(produces = Array("image/jpeg"))
    def background(resp: HttpServletResponse) {
        val random = Random.nextInt(18) + 1
        val input = getClass.getResourceAsStream("/background/loginbg" + random + ".jpg")
        IOUtils.copy(input, resp.getOutputStream)
    }
}
