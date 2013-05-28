package com.liferay.scalapress.obj.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.plugin.ecommerce.{ShoppingPlugin, ShoppingPluginDao}
import org.mockito.Mockito
import com.liferay.scalapress.enums.StockMethod

/** @author Stephen Samuel */
class StockTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

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

    test("manual stock shows number as is") {
        plugin.stockMethod = StockMethod.Manual
        val sreq = ScalapressRequest(req, context).withObject(obj)
        val actual = new ObjectStockTag().render(sreq, Map.empty).get
        assert("25" === actual)
    }

    test("InOut stock overrides > 1 to 1") {
        plugin.stockMethod = StockMethod.InOut
        val sreq = ScalapressRequest(req, context).withObject(obj)
        val actual = new ObjectStockTag().render(sreq, Map.empty).get
        assert("1" === actual)
    }
}
