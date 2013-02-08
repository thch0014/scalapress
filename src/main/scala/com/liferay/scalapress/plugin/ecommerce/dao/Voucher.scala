package com.liferay.scalapress.plugin.ecommerce.dao

import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.plugin.ecommerce.domain.Voucher

/** @author Stephen Samuel */
trait VoucherDao extends GenericDao[Voucher, java.lang.Long]

@Component
@Transactional
class VoucherDaoImpl extends GenericDaoImpl[Voucher, java.lang.Long] with VoucherDao