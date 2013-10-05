package com.cloudray.scalapress.obj.controller.admin

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.attr.AttributeType
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
class ObjectEditControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val controller = new ObjectEditController
  val form = new EditForm
  form.o = new Obj
  form.o.id = 123

  "object controller" should "strip non digits from an attribute Numerical type" in {
    val value = controller._attributeValueNormalize("abc123.4", AttributeType.Numerical)
    assert("123.4" === value)
  }

  it should "return to the images tab when removing an image" in {
    val redirect = controller.removeImage("filename", form)
    assert(redirect === "redirect:/backoffice/obj/123#tab5")
  }
}
