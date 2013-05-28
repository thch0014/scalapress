package com.cloudray.scalapress.plugin.ecommerce.dao

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.plugin.ecommerce.domain.Voucher
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait VoucherDao extends GenericDao[Voucher, java.lang.Long]

@Component
@Transactional
class VoucherDaoImpl extends GenericDaoImpl[Voucher, java.lang.Long] with VoucherDao