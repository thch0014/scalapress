package com.cloudray.scalapress.plugin.ecommerce.dao

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.plugin.ecommerce.domain.Basket
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait BasketDao extends GenericDao[Basket, String]

@Component
@Transactional
class BasketDaoImpl extends GenericDaoImpl[Basket, String] with BasketDao