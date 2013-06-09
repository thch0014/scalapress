package com.cloudray.scalapress.search

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class SearchFormDaoTest extends FunSuite with MockitoSugar {

    test("persisting a search sets id and fields") {

        val s = new SearchForm
        s.name = "funky form"

        assert(s.id == null)
        TestDatabaseContext.searchFormDao.save(s)
        assert(s.id > 0)

        val s2 = TestDatabaseContext.searchFormDao.find(s.id)
        assert("funky form" === s2.name)
    }
}
