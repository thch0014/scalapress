package com.cloudray.scalapress.framework

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class ComponentClassScannerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  "a class scanner" should "detect all payment plugins" in {
    val payments = ComponentClassScanner.paymentPlugins
    assert(4 === payments.size)
  }

  it should "detect all sections plugins" in {
    val sections = ComponentClassScanner.sections
    assert(19 === sections.size)
  }

  it should "detect all tags" in {
    val tags = ComponentClassScanner.tags
    assert(103 === tags.size)
  }
}
