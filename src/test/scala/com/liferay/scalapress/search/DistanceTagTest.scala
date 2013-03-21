package com.liferay.scalapress.search

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.util.geo.GPS

/** @author Stephen Samuel */
class DistanceTagTest extends FunSuite with MockitoSugar {

    test("distance happy path") {
        val gps1 = GPS(100, 200)
        val gps2 = GPS(200, 300)

    }
}
