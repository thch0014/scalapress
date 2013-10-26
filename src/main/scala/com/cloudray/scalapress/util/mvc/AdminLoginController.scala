package com.cloudray.scalapress.util.mvc

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice-login"))
class AdminLoginController {

  @RequestMapping
  def login = "admin/login.vm"
}
