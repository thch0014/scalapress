package com.cloudray.scalapress.plugin.ecommerce.shopping.dao

import com.cloudray.scalapress.plugin.ecommerce.domain.Address
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait AddressDao extends GenericDao[Address, java.lang.Long]

@Component
@Transactional
class AddressDaoImpl extends GenericDaoImpl[Address, java.lang.Long] with AddressDao