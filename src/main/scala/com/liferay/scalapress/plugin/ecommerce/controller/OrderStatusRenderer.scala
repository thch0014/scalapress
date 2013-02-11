package com.liferay.scalapress.plugin.ecommerce.controller

/** @author Stephen Samuel */
object OrderStatusRenderer {

    def form =
        <form class="form-horizontal" method="POST">
            <div class="control-group">
                <label class="control-label" for="orderId">Email</label>
                <div class="controls">
                    <input type="text" name="orderId" placeholder="Enter order id"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="email">Email</label>
                <div class="controls">
                    <input type="password" name="email" placeholder="Enter the order email"/>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn">Check Status</button>
                </div>
            </div>
        </form>
          .toString()

}
