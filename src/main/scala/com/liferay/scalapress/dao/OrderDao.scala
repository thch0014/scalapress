package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.plugin.ecommerce.domain.Order
import com.googlecode.genericdao.search.Search
import com.sksamuel.scoot.soa.Page
import com.liferay.scalapress.plugin.ecommerce.controller.admin.OrderQuery

/** @author Stephen Samuel */

trait OrderDao extends GenericDao[Order, java.lang.Long] {
    def search(q: OrderQuery): Page[Order]
}

@Component
@Transactional
class OrderDaoImpl extends GenericDaoImpl[Order, java.lang.Long] with OrderDao {
    def search(q: OrderQuery): Page[Order] = {

        val s = new Search(classOf[Order]).setMaxResults(q.pageSize).setFirstResult(q.offset).addSort("id", true)
        q.orderId.filter(_.trim.length > 0).foreach(t => {
            s.addFilterEqual("id", t.toLong)
        })
        q.status.filter(_.trim.length > 0).foreach(t => {
            s.addFilterEqual("status", t)
        })
        q.name.filter(_.trim.length > 0).foreach(t => {
            s.addFetch("account")
            s.addFilterLike("account.name", "%" + t + "%")
        })

        val result = searchAndCount(s)
        Page(result.getResult, q.pageNumber, q.pageSize, result.getTotalCount)
    }
}
