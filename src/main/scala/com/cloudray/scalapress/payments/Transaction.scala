package com.cloudray.scalapress.payments

import javax.persistence.{JoinColumn, GenerationType, GeneratedValue, Id, Column, Table, Entity}
import org.joda.time.{DateTimeZone, DateTime}
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Table(name = "payments")
@Entity
class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @BeanProperty
  var amount: Int = _
  def amountDecimal = amount / 100.0

  @Column(name = "authCode", nullable = true)
  @BeanProperty
  var authCode: String = _

  @BeanProperty
  var payerStatus: String = _

  @BeanProperty
  var date: Long = _

  /**
   * Any details regarding the transaction that the provider returned, eg why it was declined.
   * or
   */
  @Column(name = "details", nullable = true)
  @BeanProperty
  var details: String = _

  @Column(name = "currency", nullable = true)
  @BeanProperty
  var currency: String = _

  @Column(name = "cardType", nullable = true)
  @BeanProperty
  var cardType: String = _

  /**
   * The status of the transaction, eg "completed" or "preauthorized".
   */
  @Column(name = "status", nullable = true)
  @BeanProperty
  var status: String = _

  /**
   * The name of the person that made the transaction, if available.
   * Not all processors will supply this.
   */
  @Column(name = "payee", nullable = true)
  @BeanProperty
  var payee: String = _

  /**
   * The email of the person that made the transaction, if available.
   * Not all processors will supply this.
   */
  @Column(name = "payeeEmail", nullable = true)
  @BeanProperty
  var payeeEmail: String = _

  @JoinColumn(name = "account", nullable = true)
  @BeanProperty
  var account: String = _

  @Column(name = "orderid", nullable = true)
  @BeanProperty
  var order: String = _

  /**
   * The processor that processed the transaction.
   * Should be a simple unique human readable string of the payment processor, eg "Paypal"
   */
  @Column(name = "paymentType", nullable = true)
  @BeanProperty
  var processor: String = _

  /**
   * Human readable recommendation from the processor as to a transasctions fraud level.
   * For example, "deny" or "accept". The value is meant to be used as a guide only
   * and the accuracy will vary from processor to processor.
   *
   * This field may be null if the processor does not provide such checks for this
   * particular transaction or for any transactions.
   */
  @Column(name = "fraudHint", nullable = true)
  @BeanProperty
  var fraudHint: String = _

  /**
   * Human readable details of the security checks performed by the processor.
   * For example might contain if the address matched the card holders address.
   * Each processor is free to populate this field with whatever information it has available.
   *
   * This field may be null if no checks were performed.
   */
  @Column(name = "securityCheck", nullable = true)
  @BeanProperty
  var securityCheck: String = _

  /**
   * An id issued by the processor for this transaction.
   * Should be unique for all transactions from the same processor.
   */
  @Column(name = "processorTransactionId")
  @BeanProperty
  var transactionId: String = _

  @Column(name = "ipAddress")
  @BeanProperty
  var ipAddress: String = _

  override def toString: String = s"Transaction [id=$id transactionId=$transactionId processor=$processor date=$date]"
}

object Transaction {
  def apply(transactionId: String, processor: String, amount: Int, status: String) = {
    val payment = new Transaction
    payment.date = new DateTime(DateTimeZone.UTC).getMillis
    payment.transactionId = transactionId
    payment.amount = amount
    payment.processor = processor
    payment.status = status
    payment
  }
}