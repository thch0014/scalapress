package com.liferay.scalapress.util.geo

import org.scalatest.FunSuite

/** @author Stephen Samuel */
class PostcodeTest extends FunSuite {

    test("that a postcode is trimmed to the postcode area") {
        assert(Postcode._postcodeArea("sw109nj") === "sw10")
        assert(Postcode._postcodeArea("sw1") === "sw1")
        assert(Postcode._postcodeArea("S803GB") === "S80")
        assert(Postcode._postcodeArea("sw10") === "sw10")
        assert(Postcode._postcodeArea("DE45 1AH") === "DE45")
        assert(Postcode._postcodeArea("TQ9 6LW") === "TQ9")
    }
}
