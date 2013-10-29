package com.cloudray.scalapress.obj.controller

import org.scalatest.{ShouldMatchers, OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.obj.{ObjectType, Item}
import com.cloudray.scalapress.util.mvc.NotFoundException

/** @author Stephen Samuel */
class ObjectControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest with ShouldMatchers {

  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]

  val obj = new Item
  obj.objectType = new ObjectType
  obj.objectType.name = "products"

  val controller = new ObjectController

  "an object controller" should "throw an exception for hidden object types" in {
    evaluating {
      obj.objectType.hidden = true
      controller.view(obj, req, resp)
    } should produce[NotFoundException]
  }
}
