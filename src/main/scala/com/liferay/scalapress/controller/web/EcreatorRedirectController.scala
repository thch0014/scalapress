package com.liferay.scalapress.controller.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping}

/** @author Stephen Samuel */
@Controller
class EcreatorRedirectController {

    @RequestMapping(Array("c.do"))
    def category(@RequestParam("category") categoryId: Long) = "redirect:/folder/" + categoryId

    @RequestMapping(Array("item.do"))
    def item(@RequestParam("item") itemId: Long) = "redirect:/object/" + itemId
}
