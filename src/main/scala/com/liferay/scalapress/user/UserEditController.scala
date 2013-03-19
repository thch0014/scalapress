package com.liferay.scalapress.user

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.liferay.scalapress.user.{User, UserDao}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/user/{id}"))
class UserEditController {

    @Autowired var userDao: UserDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var passwordEncoder: PasswordEncoder = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute user: User) = "admin/user/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute user: User) = {

        Option(user.changePassword).map(_.trim).filter(_.length > 0).foreach(pass => {
            user.passwordHash = passwordEncoder.encodePassword(pass, null)
        })

        userDao.save(user)
        edit(user)
    }

    @ModelAttribute def folder(@PathVariable("id") id: Long) = userDao.find(id)
}
