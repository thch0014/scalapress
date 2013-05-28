package com.cloudray.scalapress.plugin.feed.rightmove

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class RightMoveScraperTest extends FunSuite with MockitoSugar {

    val scraper = new RightMoveScraper
    scraper.MaxResults = 20
    val properties = scraper.scrape(2496)

    test("scraper brings back properties for valid outcode") {
        assert(properties.size > 0)
    }

    test("all properties have an id") {
        assert(properties.forall(_.id != null))
    }

    test("all properties have an address") {
        assert(properties.forall(_.address != null))
    }

    test("all properties have a propertyType") {
        assert(properties.forall(_.propertyType != null))
    }

    test("all properties have a status") {
        assert(properties.forall(_.status != null))
    }
}
