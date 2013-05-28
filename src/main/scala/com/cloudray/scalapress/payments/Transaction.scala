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
    @BeanProperty var id: Long = _

    @BeanProperty var amount: Int = _
    def amountDecimal = amount / 100.0

    @BeanProperty var authCode: String = _

    @BeanProperty var payerStatus: String = _

    @BeanProperty var date: Long = _

    @BeanProperty var details: String = _

    @BeanProperty var currency: String = _

    @BeanProperty var status: String = _

    @BeanProperty var payee: String = _

    @BeanProperty var payeeEmail: String = _

    @JoinColumn(name = "account", nullable = true)
    @BeanProperty var account: String = _

    @Column(name = "orderid", nullable = true)
    @BeanProperty var order: String = _

    @Column(name = "paymentType", nullable = true)
    @BeanProperty var paymentProcessor: String = _

    @BeanProperty var transactionType: String = _

    @Column(name = "processorTransactionId")
    @BeanProperty var transactionId: String = _

    @Column(name = "ipAddress")
    @BeanProperty var ipAddress: String = _
}

object Transaction {
    def apply(transactionId: String, paymentProcessorName: String, amount: Int) = {
        val payment = new Transaction
        payment.date = new DateTime(DateTimeZone.UTC).getMillis
        payment.transactionId = transactionId
        payment.amount = amount
        payment.paymentProcessor = paymentProcessorName
        payment
    }
}