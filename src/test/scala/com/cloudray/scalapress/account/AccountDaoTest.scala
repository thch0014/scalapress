package com.cloudray.scalapress.account

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class AccountDaoTest extends FunSuite with MockitoSugar {

  test("persisting an account can be retrieved by id") {

    val acc = new Account
    acc.name = "super account"
    acc.email = "email@domain.com"
    acc.status = "status"

    TestDatabaseContext.accountDao.save(acc)

    val acc2 = TestDatabaseContext.accountDao.find(acc.id)
    assert("super account" === acc2.name)
  }

  test("email lookup happy path") {

    val acc = new Account
    acc.name = "name"
    acc.email = "sammy@sambo.com"
    acc.status = "status"

    TestDatabaseContext.accountDao.save(acc)

    val acc2 = TestDatabaseContext.accountDao.byEmail("sammy@sambo.com")
    assert(acc.id === acc2.get.id)
  }

  test("account query by status lookup") {

    val acc = new Account
    acc.id = 12
    acc.name = "name"
    acc.email = "email@domain.com"
    acc.status = "relaxed"

    TestDatabaseContext.accountDao.save(acc)

    val q = new AccountQuery().withStatus("relaxed")

    val page = TestDatabaseContext.accountDao.search(q)
    assert(page.results.size === 1)
    assert(acc.id === page.results(0).id)
  }

  test("account query by name lookup") {

    val acc = new Account
    acc.id = 13
    acc.name = "sammy the snake"
    acc.email = "email@domain.com"
    acc.status = "status"

    TestDatabaseContext.accountDao.save(acc)

    val q = new AccountQuery().withName("sammy the snake")

    val page = TestDatabaseContext.accountDao.search(q)
    assert(page.results.size === 1)
    assert(acc.id === page.results(0).id)
  }
}
