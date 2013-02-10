package com.liferay.scalapress.plugin.ecommerce.controller

/** @author Stephen Samuel */
object OrderStatusRenderer {

    def form = <form method="POST">
        <input type="text" name="orderId"/>
        <input type="text" name="email"/>
    </form>.toString()

}
