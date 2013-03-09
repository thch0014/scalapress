package com.liferay.scalapress.controller.web

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
    //def obj(@PathVariable("id") id: Int): String = "forward:/obj.c?item=" + id

    @RequestMapping(Array("obj-{id:\\d+}-{name}"))
    def obj2(@PathVariable("id") id: Int): String = "forward:/obj/" + id
    //def obj2(@PathVariable("id") id: Int): String = "forward:/obj.c?item=" + id

    @RequestMapping(Array("folder-{id:\\d+}-{name}"))
    def folder(@PathVariable("id") id: Int): String = "forward:/folder/" + id
    //def folder(@PathVariable("id") id: Int): String = "forward:/folder.c?category=" + id
}
