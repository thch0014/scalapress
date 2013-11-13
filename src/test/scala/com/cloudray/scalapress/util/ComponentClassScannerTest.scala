package com.cloudray.scalapress.util

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.framework.ComponentClassScanner

/** @author Stephen Samuel */
class ComponentClassScannerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  test("scanner picks up all tags") {
    val tags = ComponentClassScanner.tags
    assert(102 === tags.size)
  }
}
