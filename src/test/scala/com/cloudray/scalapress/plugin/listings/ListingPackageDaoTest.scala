package com.cloudray.scalapress.plugin.listings

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import com.cloudray.scalapress.plugin.listings.domain.{ListingProcess, ListingPackage}

/** @author Stephen Samuel */
class ListingPackageDaoTest extends FunSuite with MockitoSugar {

    val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

    val listingPackageDao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[ListingPackageDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[ListingPackageDao]

    val listingProcessDao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[ListingProcessDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[ListingProcessDao]

    test("persisting a listing Package sets id and fields") {

        val lp = new ListingPackage
        lp.name = "lusty listing package"

        assert(lp.id === 0)
        listingPackageDao.save(lp)
        assert(lp.id > 0)

        val lp2 = listingPackageDao.find(lp.id)
        assert("lusty listing package" === lp2.name)
    }

    test("persisting a listing process sets fields") {

        val lp = new ListingProcess
        lp.email = "sam@bozo.com"
        lp.title = "list me up baby"
        lp.sessionId = "ab341299de-6334bcef-45345"

        listingProcessDao.save(lp)
        assert(lp.sessionId != null)

        val lp2 = listingProcessDao.find("ab341299de-6334bcef-45345")
        assert("ab341299de-6334bcef-45345" === lp2.sessionId)
        assert("sam@bozo.com" === lp2.email)
        assert("list me up baby" === lp2.title)
    }
}
