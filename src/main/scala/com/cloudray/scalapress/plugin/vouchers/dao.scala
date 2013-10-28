package com.cloudray.scalapress.plugin.vouchers

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait VoucherDao extends GenericDao[Voucher, Long]

@Component
@Transactional
class VoucherDaoImpl extends GenericDaoImpl[Voucher, java.lang.Long] with VoucherDao

