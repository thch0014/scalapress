package com.cloudray.scalapress.plugin.url.simpleurls

import org.springframework.web.bind.annotation.{RequestMapping, PathVariable}
import org.springframework.stereotype.Controller

/** @author Stephen Samuel */
@Controller
@RequestMapping
class SimpleUrlForwardController {

    @RequestMapping(Array("o{id:\\d+}-{name}"))
    def obj(@PathVariable("id") id: Int): String = "forward:/object/" + id

    @RequestMapping(Array("f{id:\\d+}-{name}"))
    def folder(@PathVariable("id") id: Int): String = "forward:/folder/" + id
}
