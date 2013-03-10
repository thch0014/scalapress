package com.liferay.scalapress.plugin.ecommerce.dao

import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import com.liferay.scalapress.plugin.ecommerce.domain.Transaction
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */
trait TransactionDao extends GenericDao[Transaction, java.lang.Long]

@Component
@Transactional
class TransactionDaoImpl extends GenericDaoImpl[Transaction, java.lang.Long] with TransactionDao