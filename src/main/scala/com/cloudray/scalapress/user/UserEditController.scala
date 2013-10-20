package com.cloudray.scalapress.user

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.security.authentication.encoding.PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/user/{id}"))
@Autowired
class UserEditController(userDao: UserDao,
                         passwordEncoder: PasswordEncoder) {

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def edit(@ModelAttribute user: User) = "admin/user/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
  def save(@ModelAttribute user: User) = {

    Option(user.changePassword).map(_.trim).filterNot(_.isEmpty).foreach(pass => {
      user.passwordHash = passwordEncoder.encodePassword(pass, null)
    })

    userDao.save(user)
    "redirect:/backoffice/user"
  }

  @ModelAttribute def folder(@PathVariable("id") id: Long) = userDao.find(id)
}
