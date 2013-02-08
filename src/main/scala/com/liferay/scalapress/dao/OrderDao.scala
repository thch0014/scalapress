package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.plugin.ecommerce.domain.Order

/** @author Stephen Samuel */

trait OrderDao extends GenericDao[Order, java.lang.Long]

@Component
@Transactional
class OrderDaoImpl extends GenericDaoImpl[Order, java.lang.Long] with OrderDao
