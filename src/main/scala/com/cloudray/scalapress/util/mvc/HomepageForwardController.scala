package com.cloudray.scalapress.util.mvc

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.stereotype.Controller

/** @author Stephen Samuel */
@Controller
@RequestMapping
class HomepageForwardController {

    @RequestMapping
    def homepage = "forward:/folder"
}
