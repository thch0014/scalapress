package com.liferay.scalapress.plugin.ecommerce.dao

import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import com.liferay.scalapress.plugin.ecommerce.domain.Address
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */
trait AddressDao extends GenericDao[Address, java.lang.Long]

@Component
@Transactional
class AddressDaoImpl extends GenericDaoImpl[Address, java.lang.Long] with AddressDao