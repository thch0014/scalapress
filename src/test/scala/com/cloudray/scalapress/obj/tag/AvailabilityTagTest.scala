package com.cloudray.scalapress.obj.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.plugin.ecommerce.{ShoppingPlugin, ShoppingPluginDao}
import org.mockito.Mockito
import com.cloudray.scalapress.enums.StockMethod

/** @author Stephen Samuel */
class AvailabilityTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = mock[ScalapressContext]
    val req = mock[HttpServletRequest]
    val plugin = new ShoppingPlugin
    val dao = mock[ShoppingPluginDao]
    Mockito.when(context.bean[ShoppingPluginDao]).thenReturn(dao)
    Mockito.when(dao.get).thenReturn(plugin)

    val obj = new Obj
    obj.id = 12
    obj.name = "meatballs"
    obj.stock = 25

    val tag = new ObjectAvailabilityTag()

    test("when stock is disabled this tag renders None") {
        plugin.stockMethod = StockMethod.Off
        val sreq = ScalapressRequest(req, context).withObject(obj)
        val actual = tag.render(sreq, Map.empty)
        assert(actual.isEmpty)
    }

    test("given stock > 0 then the in stock message renders") {
        plugin.stockMethod = StockMethod.Automatic
        val sreq = ScalapressRequest(req, context).withObject(obj)
        val actual = tag.render(sreq, Map.empty)
        assert("In Stock" === actual.get)
    }

    test("given stock = 0 then the objects out of stock message renders") {
        obj.stock = 0
        obj.outStockMsg = "no more :("
        plugin.stockMethod = StockMethod.Manual
        val sreq = ScalapressRequest(req, context).withObject(obj)
        val actual = tag.render(sreq, Map.empty)
        assert("no more :(" === actual.get)
    }

    test("given stock = 0 and no out of stock message in the object then the plugin out of stock message renders") {
        obj.stock = 0
        obj.outStockMsg = null
        plugin.stockMethod = StockMethod.Manual
        plugin.outOfStockMessage = "we're out everywhere"
        val sreq = ScalapressRequest(req, context).withObject(obj)
        val actual = tag.render(sreq, Map.empty)
        assert("we're out everywhere" === actual.get)
    }
}