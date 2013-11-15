package com.cloudray.scalapress.account

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext
import com.cloudray.scalapress.account.controller.Datum
import scala.util.Random

/** @author Stephen Samuel */
class AccountDaoTest extends FunSuite with MockitoSugar {

  test("persisting an account can be retrieved by id") {

    val acc = new Account
    acc.name = "super account"
    acc.email = "email@domain.com"
    acc.status = "status"
    acc.id = 10

    TestDatabaseContext.accountDao.save(acc)

    val acc2 = TestDatabaseContext.accountDao.find(acc.id)
    assert("super account" === acc2.name)
  }

  test("email lookup happy path") {

    val acc = new Account
    acc.name = "name"
    acc.email = "sammy@sambo.com"
    acc.status = "status"
    acc.id = 11

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

  test("typeahead happy path") {

    val acc1 = new Account
    acc1.id = Math.abs(Random.nextInt())
    acc1.name = "mr merlot"

    val acc2 = new Account
    acc2.id = Math.abs(Random.nextInt())
    acc2.name = "dr cab"

    val acc3 = new Account
    acc3.id = Math.abs(Random.nextInt())
    acc3.name = "mr shiraz"

    TestDatabaseContext.accountDao.save(acc1)
    TestDatabaseContext.accountDao.save(acc2)
    TestDatabaseContext.accountDao.save(acc3)

    val datums = TestDatabaseContext.accountDao.typeAhead("mr")
    assert(2 === datums.size)
    assert(datums === Array(Datum(acc1.name, acc1.id.toString), Datum(acc3.name, acc3.id.toString)))
  }
}
