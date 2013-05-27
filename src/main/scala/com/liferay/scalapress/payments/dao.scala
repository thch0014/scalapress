package com.liferay.scalapress.payments

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait TransactionDao extends GenericDao[Transaction, java.lang.Long]

@Component
@Transactional
class TransactionDaoImpl extends GenericDaoImpl[Transaction, java.lang.Long] with TransactionDao