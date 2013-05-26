package com.liferay.scalapress.plugin.ecommerce

import com.liferay.scalapress.plugin.payments.Transaction
import com.liferay.scalapress.plugin.ecommerce.domain.Order
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{Logging, ScalapressContext}

/** @author Stephen Samuel */
@Component
class OrderCallbackProcessor extends Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var shoppingPlugin: ShoppingPlugin = _
    @Autowired var orderDao: OrderDao = _

    def _payment(tx: Transaction, order: Order) {
        logger.debug("Setting order [{}] as paid and associating tx [{}]", order.id, tx.id)
        order.status = Order.STATUS_PAID
        order.payments.add(tx)
        orderDao.save(order)
    }

    def callback(tx: Transaction, order: Order) {
        _payment(tx, order)
    }
}
