package com.cloudray.scalapress.user

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class UserDaoTest extends FunSuite with MockitoSugar {

  test("if no users exist then ensureAUser creates default user") {

    val init = new UserDaoInit
    init.userDao = TestDatabaseContext.userDao
    init.ensureAUser()

    val user = TestDatabaseContext.userDao.findAll.head
    assert(init.defaultUser.name === user.name)
    assert(init.defaultUser.username === user.username)
    assert(init.defaultUser.passwordHash === user.passwordHash)
  }

  test("user can be retrieved by username") {
    val user = TestDatabaseContext.userDao.byUsername("admin")
    assert("admin" === user.username)
  }
}
