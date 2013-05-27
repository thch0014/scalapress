package com.liferay.scalapress.plugin.ecommerce

import com.liferay.scalapress.plugin.payments.{PaymentCallback, Transaction}
import com.liferay.scalapress.plugin.ecommerce.domain.Order
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{Callback, Logging, ScalapressContext}

/** @author Stephen Samuel */
@Component
@Callback("Order")
class OrderCallbackProcessor extends PaymentCallback with Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var shoppingPluginDao: ShoppingPluginDao = _
    @Autowired var orderDao: OrderDao = _
    @Autowired var orderCustomerNotificationService: OrderCustomerNotificationService = _

    override def callback(tx: Transaction, id: String) {
        val order = orderDao.find(id.toLong)
        callback(tx, order)
    }

    def callback(tx: Transaction, order: Order) {
        _assignTx(tx, order)
        _email(order)
    }

    def _assignTx(tx: Transaction, order: Order) {
        logger.debug("Setting order [{}] as paid and associating tx [{}]", order.id, tx.id)
        if (order.status == Order.STATUS_NEW) {
            order.status = Order.STATUS_PAID
        }
        order.payments.add(tx)
        orderDao.save(order)
    }

    def _email(order: Order) {
        logger.debug("Sending order placed email")
        orderCustomerNotificationService.orderPlaced(order)
    }
}
