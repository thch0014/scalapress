package com.liferay.scalapress.controller.web.security

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/** @author Stephen Samuel */
@Controller
@RequestMapping
class LoginController {

    @RequestMapping(Array("login"))
    def login = "login"

    @RequestMapping(Array("register"))
    def register = "register"
}
