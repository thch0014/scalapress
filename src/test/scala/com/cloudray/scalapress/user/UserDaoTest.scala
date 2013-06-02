package com.cloudray.scalapress.user

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.AutowireCapableBeanFactory

/** @author Stephen Samuel */
class UserDaoTest extends FunSuite with MockitoSugar {

    val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

    val dao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[UserDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[UserDao]

    test("if no users exist then ensureAUser creates default user") {

        val init = new UserDaoInit
        init.userDao = dao
        init.ensureAUser()

        val user = dao.findAll().head
        assert(init.defaultUser.name === user.name)
        assert(init.defaultUser.username === user.username)
        assert(init.defaultUser.passwordHash === user.passwordHash)
    }

    test("user can be retrieved by username") {
        val user = dao.byUsername("admin")
        assert("admin" === user.username)
    }
}
