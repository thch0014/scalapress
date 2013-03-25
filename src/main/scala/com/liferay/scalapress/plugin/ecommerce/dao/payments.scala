package com.liferay.scalapress.plugin.ecommerce.dao

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}
import com.liferay.scalapress.plugin.payments.Transaction

/** @author Stephen Samuel */
trait TransactionDao extends GenericDao[Transaction, java.lang.Long]

@Component
@Transactional
class TransactionDaoImpl extends GenericDaoImpl[Transaction, java.lang.Long] with TransactionDao