package com.cloudray.scalapress.item.controller

import org.scalatest.{ShouldMatchers, OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.cloudray.scalapress.item.{ItemDao, ItemType, Item}
import com.cloudray.scalapress.util.mvc.NotFoundException
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class ItemControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest with ShouldMatchers {

  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]

  val obj = new Item
  obj.objectType = new ItemType
  obj.objectType.name = "products"

  val itemDao = mock[ItemDao]
  val themeService = mock[ThemeService]
  val context = new ScalapressContext

  val controller = new ItemController(itemDao, themeService, context)

  "an object controller" should "throw an exception for hidden object types" in {
    evaluating {
      obj.objectType.hidden = true
      controller.view(obj, req, resp)
    } should produce[NotFoundException]
  }
}
