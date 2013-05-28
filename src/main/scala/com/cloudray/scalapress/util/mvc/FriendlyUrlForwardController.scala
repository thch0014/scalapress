package com.cloudray.scalapress.util.mvc

import org.springframework.web.bind.annotation.{RequestMapping, PathVariable}
import org.springframework.stereotype.Controller

/** @author Stephen Samuel */
@Controller
@RequestMapping
class FriendlyUrlForwardController {

    @RequestMapping
    def homepage = "forward:/folder"

    @RequestMapping(Array("object-{id:\\d+}-{name}"))
    def obj(@PathVariable("id") id: Int): String = "forward:/object/" + id

    @RequestMapping(Array("obj-{id:\\d+}-{name}"))
    def obj2(@PathVariable("id") id: Int): String = "forward:/object/" + id

    @RequestMapping(Array("folder-{id:\\d+}-{name}"))
    def folder(@PathVariable("id") id: Int): String = "forward:/folder/" + id
}
