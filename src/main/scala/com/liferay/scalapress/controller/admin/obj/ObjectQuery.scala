package com.liferay.scalapress.controller.admin.obj

import com.liferay.scalapress.Page
import com.liferay.scalapress.domain.Obj

/** @author Stephen Samuel */
case class ObjectQuery(objectType: Option[Long] = None,
                       accountId: Option[Long] = None,
                       pageNumber: Int = Page.FirstPage,
                       pageSize: Int = Page.DefaultPageSize) {
    def withType(l: Long) = copy(objectType = Option(l))
    def offset: Int = (pageNumber - 1) * pageSize
    def withPageNumber(pageNumber: Int) = copy(pageNumber = pageNumber)
    def withPageSize(pageSize: Int) = copy(pageNumber = pageSize)
}
