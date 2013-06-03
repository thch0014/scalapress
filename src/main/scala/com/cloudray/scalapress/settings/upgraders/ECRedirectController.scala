package com.cloudray.scalapress.settings.upgraders

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping}
import com.cloudray.scalapress.util.UrlGenerator
import com.cloudray.scalapress.ScalapressContext
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
class ECRedirectController {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(Array("c.do"))
    def category(@RequestParam("category") id: Long) = {
        val folder = context.folderDao.find(id)
        "redirect:" + UrlGenerator.link(folder)
    }

    @RequestMapping(Array("item.do"))
    def item(@RequestParam("item") id: Long) = {
        val obj = context.objectDao.find(id)
        "redirect:" + UrlGenerator.link(obj)
    }
}
