package com.cloudray.scalapress.plugin.listings

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.listings.domain.{ListingProcess, ListingPackage}
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class ListingPackageDaoTest extends FunSuite with MockitoSugar {

    test("persisting a listing Package sets id and fields") {

        val lp = new ListingPackage
        lp.name = "lusty listing package"

        assert(lp.id === 0)
        TestDatabaseContext.listingPackageDao.save(lp)
        assert(lp.id > 0)

        val lp2 = TestDatabaseContext.listingPackageDao.find(lp.id)
        assert("lusty listing package" === lp2.name)
    }

    test("persisting a listing process sets fields") {

        val lp = new ListingProcess
        lp.email = "sam@bozo.com"
        lp.title = "list me up baby"
        lp.sessionId = "ab341299de-6334bcef-45345"

        TestDatabaseContext.listingProcessDao.save(lp)
        assert(lp.sessionId != null)

        val lp2 = TestDatabaseContext.listingProcessDao.find("ab341299de-6334bcef-45345")
        assert("ab341299de-6334bcef-45345" === lp2.sessionId)
        assert("sam@bozo.com" === lp2.email)
        assert("list me up baby" === lp2.title)
    }
}
