package com.liferay.scalapress.controller.admin.users

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.domain.User
import com.liferay.scalapress.user.UserDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/user"))
class UserListController {

    @Autowired var userDao: UserDao = _
    @Autowired var context: ScalapressContext = _

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

    import scala.collection.JavaConverters._

    @ModelAttribute("users") def users = userDao.findAll().asJava
}
