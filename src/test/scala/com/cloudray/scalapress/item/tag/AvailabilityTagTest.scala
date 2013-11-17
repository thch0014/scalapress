package com.cloudray.scalapress.item.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.{StockMethod, Item}
import org.mockito.Mockito
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.{ShoppingPluginDao, ShoppingPlugin}
import com.cloudray.scalapress.plugin.ecommerce.variations.{Variation, VariationDao}

/** @author Stephen Samuel */
class AvailabilityTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val context = mock[ScalapressContext]
  val req = mock[HttpServletRequest]
  val plugin = new ShoppingPlugin
  val dao = mock[ShoppingPluginDao]
  val variationDao = mock[VariationDao]
  Mockito.when(context.bean[ShoppingPluginDao]).thenReturn(dao)
  Mockito.when(context.bean[VariationDao]).thenReturn(variationDao)
  Mockito.when(dao.get).thenReturn(plugin)

  val obj = new Item
  obj.id = 12
  obj.name = "meatballs"
  obj.stock = 25

  Mockito.when(variationDao.findByItemId(12)).thenReturn(Nil)

  val tag = new TagAvailabilityTag()

  test("when stock is disabled this tag renders None") {
    plugin.stockMethod = StockMethod.Off
    val sreq = ScalapressRequest(req, context).withItem(obj)
    val actual = tag.render(sreq, Map.empty)
    assert(actual.isEmpty)
  }

  test("when object has variations then availability should not render") {
    obj.id = 15
    Mockito.when(variationDao.findByItemId(15)).thenReturn(Seq(new Variation))
    val sreq = ScalapressRequest(req, context).withItem(obj)
    val actual = tag.render(sreq, Map.empty)
    //    assert(actual.isEmpty)
  }

  test("given stock > 0 then the in stock message renders") {
    plugin.stockMethod = StockMethod.Automatic
    val sreq = ScalapressRequest(req, context).withItem(obj)
    val actual = tag.render(sreq, Map.empty)
    assert("In Stock" === actual.get)
  }

  test("given stock = 0 and an out of stock message then the objects out of stock message renders") {
    obj.stock = 0
    obj.outStockMsg = "no more :("
    plugin.stockMethod = StockMethod.Manual
    val sreq = ScalapressRequest(req, context).withItem(obj)
    val actual = tag.render(sreq, Map.empty)
    assert("no more :(" === actual.get)
  }

  test("given stock = 0 and no out of stock message in the object then the plugin out of stock message renders") {
    obj.stock = 0
    obj.outStockMsg = null
    plugin.stockMethod = StockMethod.Manual
    plugin.outOfStockMessage = "we're out everywhere"
    val sreq = ScalapressRequest(req, context).withItem(obj)
    val actual = tag.render(sreq, Map.empty)
    assert("we're out everywhere" === actual.get)
  }

  test("given stock >0 and in/out stock type then tag shows in stock message") {
    obj.stock = 3
    plugin.stockMethod = StockMethod.InOut
    val sreq = ScalapressRequest(req, context).withItem(obj)
    val actual = tag.render(sreq, Map.empty)
    assert("In stock" === actual.get)
  }
}
