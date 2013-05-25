package com.liferay.scalapress.plugin.payments

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait TransactionDao extends GenericDao[Transaction, java.lang.Long]

@Component
@Transactional
class TransactionDaoImpl extends GenericDaoImpl[Transaction, java.lang.Long] with TransactionDao

trait PurchaseSessionDao extends GenericDao[PurchaseSession, java.lang.Long]

@Component
@Transactional
class PurchaseSessionDaoImpl extends GenericDaoImpl[PurchaseSession, java.lang.Long] with PurchaseSessionDao