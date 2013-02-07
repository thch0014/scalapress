package com.liferay.scalapress.dao.ecommerce

import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.plugin.ecommerce.domain.Basket

/** @author Stephen Samuel */
trait BasketDao extends GenericDao[Basket, String]

@Component
@Transactional
class BasketDaoImpl extends GenericDaoImpl[Basket, String] with BasketDao