package com.liferay.scalapress.plugin.account.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/** @author Stephen Samuel */
@Controller
@RequestMapping
class LoginController {

    @RequestMapping(Array("login"))
    def login = "login"
}
