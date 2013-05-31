package com.cloudray.scalapress.search

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.AutowireCapableBeanFactory

/** @author Stephen Samuel */
class SavedSearchDaoTest extends FunSuite with MockitoSugar {

    val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

    val savedSearchDao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[SavedSearchDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[SavedSearchDao]

    test("persisting a search sets id and fields") {

        val s = new SavedSearch
        s.name = "super duper search"
        s.maxResults = 14
        s.prefix = "preffy"
        s.status = "liveeee"
        s.imageOnly = true
        s.searchFolders = "1,2,3"

        assert(s.id == null)
        savedSearchDao.save(s)
        assert(s.id > 0)

        val s2 = savedSearchDao.find(s.id)
        assert("super duper search" === s2.name)
        assert("preffy" === s2.prefix)
        assert("liveeee" === s2.status)
        assert("1,2,3" === s2.searchFolders)
        assert(14 === s2.maxResults)
        assert(true === s2.imageOnly)
    }
}
