package com.cloudray.scalapress.item.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.{StockMethod, Item}
import org.mockito.Mockito
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.{ShoppingPluginDao, ShoppingPlugin}

/** @author Stephen Samuel */
class StockTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val context = mock[ScalapressContext]
  val req = mock[HttpServletRequest]
  val plugin = new ShoppingPlugin
  val dao = mock[ShoppingPluginDao]
  Mockito.when(context.bean[ShoppingPluginDao]).thenReturn(dao)
  Mockito.when(dao.get).thenReturn(plugin)

  val obj = new Item
  obj.id = 12
  obj.name = "meatballs"
  obj.stock = 25

  test("manual stock shows number as is") {
    plugin.stockMethod = StockMethod.Manual
    val sreq = ScalapressRequest(req, context).withItem(obj)
    val actual = new TagStockTag().render(sreq, Map.empty).get
    assert("25" === actual)
  }

  test("InOut stock overrides > 1 to 1") {
    plugin.stockMethod = StockMethod.InOut
    val sreq = ScalapressRequest(req, context).withItem(obj)
    val actual = new TagStockTag().render(sreq, Map.empty).get
    assert("1" === actual)
  }
}
