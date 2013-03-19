package com.liferay.scalapress.obj.controller.admin

import com.sksamuel.scoot.soa.PagedQuery
import reflect.BeanProperty

/** @author Stephen Samuel */
class ObjectQuery extends PagedQuery {

    @BeanProperty var typeId: Option[Long] = None
    @BeanProperty var accountId: Option[Long] = None
    @BeanProperty var status: Option[String] = None
    @BeanProperty var name: Option[String] = None

    def withAccountId(a: Option[Long]) = {
        this.accountId = a
        this
    }

    def withStatus(status: String) = {
        this.status = Option(status)
        this
    }

    def withPageSize(i: Int) = {
        this.pageSize = i
        this
    }
}
