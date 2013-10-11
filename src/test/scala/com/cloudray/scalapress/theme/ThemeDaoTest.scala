package com.cloudray.scalapress.theme

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class ThemeDaoTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  test("persisting a theme sets id and fields") {

    val t = new Theme
    t.name = "western"

    assert(t.id === 0)
    TestDatabaseContext.themeDao.save(t)
    assert(t.id > 0)

    val t2 = TestDatabaseContext.themeDao.find(t.id)
    assert("western" === t2.name)
  }

  test("persisting a markup sets id and fields") {

    val m = new Markup
    m.name = "western"
    m.between = "between"
    m.start = "ready steady go"
    m.end = "usain bolt won again"
    m.body = "arnold swarz"

    assert(m.id === 0)
    TestDatabaseContext.markupDao.save(m)
    assert(m.id > 0)

    val m2 = TestDatabaseContext.markupDao.find(m.id)
    assert("western" === m2.name)
    assert("between" === m2.between)
    assert("ready steady go" === m2.start)
    assert("usain bolt won again" === m2.end)
    assert("arnold swarz" === m2.body)
  }

  test("markup can be located by name") {

    val m = new Markup
    m.name = "find me again"
    m.between = "between"
    m.start = "ready steady go"
    m.end = "usain bolt won again"
    m.body = "arnold swarz"

    TestDatabaseContext.markupDao.save(m)
    assert(m.id > 0)

    val m2 = TestDatabaseContext.markupDao.byName("find me again")
    assert(m.id === m2.id)
  }

  test("findDefault returns the correct default") {

    val t1 = new Theme
    t1.name = "picasso"
    TestDatabaseContext.themeDao.save(t1)

    val t2 = new Theme
    t2.default = true
    t2.name = "rembrant"
    TestDatabaseContext.themeDao.save(t2)

    val default = TestDatabaseContext.themeDao.findDefault
    assert(t2.id > 0)
    assert(t2.id === default.id)
  }
}
