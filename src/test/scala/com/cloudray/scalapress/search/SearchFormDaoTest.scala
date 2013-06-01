package com.cloudray.scalapress.search

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.AutowireCapableBeanFactory

/** @author Stephen Samuel */
class SearchFormDaoTest extends FunSuite with MockitoSugar {

    val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

    val searchFormDao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[SearchFormDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[SearchFormDao]

    test("persisting a search sets id and fields") {

        val s = new SearchForm
        s.name = "funky form"

        assert(s.id == null)
        searchFormDao.save(s)
        assert(s.id > 0)

        val s2 = searchFormDao.find(s.id)
        assert("funky form" === s2.name)
    }
}
