package com.liferay.scalapress.plugin.ecommerce

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.payments.Transaction
import com.liferay.scalapress.plugin.ecommerce.domain.Order
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.settings.{Installation, InstallationDao}
import org.mockito.Mockito

/** @author Stephen Samuel */
class OrderCallbackProcessorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val tx = new Transaction
    tx.id = 12

    val order = new Order
    order.id = 346
    order.status = Order.STATUS_NEW

    val installation = new Installation

    val plugin = new ShoppingPlugin
    plugin.orderConfirmationRecipients = "newton@oxford.com euler@bridges.com"

    val callback = new OrderCallbackProcessor
    callback.context = new ScalapressContext
    callback.context.installationDao = mock[InstallationDao]
    Mockito.when(callback.context.installationDao.get).thenReturn(installation)

    callback.orderDao = mock[OrderDao]
    callback.shoppingPluginDao = mock[ShoppingPluginDao]
    Mockito.when(callback.shoppingPluginDao.get).thenReturn(plugin)
    callback.orderCustomerNotificationService = mock[OrderCustomerNotificationService]

    test("tx is added to order") {
        assert(0 === order.payments.size)
        callback.callback(tx, order)
        assert(1 === order.payments.size)
        assert(order.payments.contains(tx))
    }

    test("order status is updated to paid") {
        callback.callback(tx, order)
        assert(Order.STATUS_PAID === order.status)
    }

    test("order status is not changed if not new") {
        order.status = "what the..."
        callback.callback(tx, order)
        assert("what the..." === order.status)
    }

    test("emails are sent using the recipients from shopping plugin") {
        callback.callback(tx, order)
        Mockito.verify(callback.orderCustomerNotificationService)
          .orderPlaced(Array("newton@oxford.com", "euler@bridges.com"), order, installation)
    }
}