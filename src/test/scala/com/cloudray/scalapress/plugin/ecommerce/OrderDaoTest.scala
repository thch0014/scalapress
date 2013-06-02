package com.cloudray.scalapress.plugin.ecommerce

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import com.cloudray.scalapress.plugin.ecommerce.domain.Order

/** @author Stephen Samuel */
class OrderDaoTest extends FunSuite with MockitoSugar {

    val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

    val dao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[OrderDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[OrderDao]

    test("persisting an order assigns id") {

        val order = new Order
        order.deliveryCharge = 145
        order.vatable = true

        assert(order.id == 0)
        dao.save(order)
        assert(order.id > 0)
    }

    test("persisting an order sets all fields") {

        val order = new Order
        order.deliveryCharge = 145
        order.vatable = true
        dao.save(order)

        val order2 = dao.find(order.id)
        assert(145 === order2.deliveryCharge)
        assert(order.vatable)
    }
}
