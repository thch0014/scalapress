package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.plugin.ecommerce.domain.Order
import com.liferay.scalapress.{Page, PagedQuery}
import com.liferay.scalapress.plugin.form.Submission
import com.googlecode.genericdao.search.Search

/** @author Stephen Samuel */

trait OrderDao extends GenericDao[Order, java.lang.Long] {
    def search(q: PagedQuery): Page[Order]
}

@Component
@Transactional
class OrderDaoImpl extends GenericDaoImpl[Order, java.lang.Long] with OrderDao {
    def search(q: PagedQuery): Page[Order] = {
        val s = new Search(classOf[Order]).setMaxResults(q.pageSize).setFirstResult(q.offset).addSort("id", true)
        val result = searchAndCount(s)
        Page(result.getResult, q.pageNumber, q.pageSize, result.getTotalCount)
    }
}
