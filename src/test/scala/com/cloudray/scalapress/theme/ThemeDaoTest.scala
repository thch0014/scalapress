package com.cloudray.scalapress.theme

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.AutowireCapableBeanFactory

/** @author Stephen Samuel */
class ThemeDaoTest extends FunSuite with MockitoSugar {

    val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

    val themeDao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[ThemeDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[ThemeDao]

    val markupDao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[MarkupDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[MarkupDao]

    test("persisting a theme sets id and fields") {

        val t = new Theme
        t.name = "western"

        assert(t.id === 0)
        themeDao.save(t)
        assert(t.id > 0)

        val t2 = themeDao.find(t.id)
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
        markupDao.save(m)
        assert(m.id > 0)

        val m2 = markupDao.find(m.id)
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

        markupDao.save(m)
        assert(m.id > 0)

        val m2 = markupDao.byName("find me again")
        assert(m.id === m2.id)
    }
}
