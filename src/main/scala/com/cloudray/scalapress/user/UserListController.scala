package com.cloudray.scalapress.user

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/user"))
class UserListController(userDao: UserDao) {

  @RequestMapping(produces = Array("text/html"))
  def list = "admin/user/list.vm"

  @RequestMapping(value = Array("create"), produces = Array("text/html"))
  def create = {
    val u = new User
    u.name = "new user"
    u.username = "nousername"
    userDao.save(u)
    list
  }

  @ModelAttribute("users") def users = userDao.findAll.asJava
}
