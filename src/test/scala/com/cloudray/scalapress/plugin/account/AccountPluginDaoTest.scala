package com.cloudray.scalapress.plugin.account

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class AccountPluginDaoTest extends FunSuite with MockitoSugar {

  test("persisting a listing process sets fields") {

    val plugin = new AccountPlugin
    plugin.id = 100
    plugin.accountPageHeader = "header"
    plugin.accountPageFooter = "footer"

    TestDatabaseContext.accountPluginDao.save(plugin)

    val plugin2 = TestDatabaseContext.accountPluginDao.get
    assert("header" === plugin2.accountPageHeader)
    assert("footer" === plugin2.accountPageFooter)
  }
}
