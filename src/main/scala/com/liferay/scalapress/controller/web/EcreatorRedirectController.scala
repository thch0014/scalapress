package com.liferay.scalapress.controller.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/** @author Stephen Samuel */
@Controller
class EcreatorRedirectController {

    @RequestMapping("c.do")
    def category(@RequestMapping("category") categoryId: Long) = "redirect:/folder/" + categoryId

    @RequestMapping("item.do")
    def item(@RequestMapping("item") itemId: Long) = "redirect:/object/" + itemId
}
