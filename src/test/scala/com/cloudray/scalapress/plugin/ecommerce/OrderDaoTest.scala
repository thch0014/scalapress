package com.cloudray.scalapress.plugin.ecommerce

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import com.cloudray.scalapress.plugin.ecommerce.controller.admin.OrderQuery

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

    test("search by status") {

        val order1 = new Order
        order1.status = "live"
        dao.save(order1)

        val order2 = new Order
        order2.status = "dead"
        dao.save(order2)

        val order3 = new Order
        order3.status = "undead"
        dao.save(order3)

        val q = new OrderQuery
        q.status = Some("undead")
        val p = dao.search(q)
        assert(1 === p.results.size)
        assert(order3.id === p.results(0).id)
    }
}
