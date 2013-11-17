package com.cloudray.scalapress.plugin.ecommerce.shopping.dao

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import com.sksamuel.scoot.soa.Search
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.Address

/** @author Stephen Samuel */
trait AddressDao extends GenericDao[Address, java.lang.Long] {
  def findFromAccountId(accountId: Long): Seq[Address]
}

@Component
@Transactional
class AddressDaoImpl extends GenericDaoImpl[Address, java.lang.Long] with AddressDao {
  def findFromAccountId(accountId: Long): Seq[Address] = {
    super.search(new Search[Address].addFilterEqual("account", accountId))
  }
}