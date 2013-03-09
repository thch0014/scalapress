package com.liferay.scalapress.dao.ecommerce

import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.domain.ecommerce.Voucher

/** @author Stephen Samuel */
trait VoucherDao extends GenericDao[Voucher, java.lang.Long]

@Component
@Transactional
class VoucherDaoImpl extends GenericDaoImpl[Voucher, java.lang.Long] with VoucherDao