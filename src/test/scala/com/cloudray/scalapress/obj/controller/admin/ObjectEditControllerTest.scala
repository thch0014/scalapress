package com.cloudray.scalapress.obj.controller.admin

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.enums.AttributeType

/** @author Stephen Samuel */
class ObjectEditControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val controller = new ObjectEditController

  "object controller" should "strip non digits from an attribute Numerical type" in {
    val value = controller._attributeValueNormalize("abc123.4", AttributeType.Numerical)
    assert("123.4" === value)
  }
}
