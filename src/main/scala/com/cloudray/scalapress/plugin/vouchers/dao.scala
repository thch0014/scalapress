package com.cloudray.scalapress.plugin.vouchers

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait VoucherDao extends GenericDao[Voucher, java.lang.Long] {
  def byCode(code: String): Option[Voucher]
  def enabled: Boolean
}

@Component
@Transactional
class VoucherDaoImpl extends GenericDaoImpl[Voucher, java.lang.Long] with VoucherDao {
  def enabled: Boolean = findAll.size > 0
  def byCode(code: String): Option[Voucher] = findAll.find(_.code.toLowerCase == code.toLowerCase)
}

