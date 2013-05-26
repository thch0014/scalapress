package com.liferay.scalapress.plugin.ecommerce

import com.liferay.scalapress.plugin.payments.{PaymentCallback, Transaction}
import com.liferay.scalapress.plugin.ecommerce.domain.Order
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{Logging, ScalapressContext}

/** @author Stephen Samuel */
@Component
class OrderCallbackProcessor extends PaymentCallback with Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var shoppingPlugin: ShoppingPlugin = _
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
        order.status = Order.STATUS_PAID
        order.payments.add(tx)
        orderDao.save(order)
    }

    def _email(order: Order) {
        val recipients = Option(shoppingPlugin.orderConfirmationRecipients).getOrElse("").split(Array(',', '\n', ' '))
        logger.debug("Sending order placed email [{}]", recipients)
        orderCustomerNotificationService.orderPlaced(recipients, order, context.installationDao.get)
    }
}
