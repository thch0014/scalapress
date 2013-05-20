package com.liferay.scalapress.util

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class ComponentClassScannerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    test("scanner picks up all tags") {
        val tags = ComponentClassScanner.tags
        assert(90 === tags.size)
    }
}