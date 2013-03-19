package com.liferay.scalapress.plugin.ecommerce.domain

import javax.persistence.{JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Column, Table, Entity}
import reflect.BeanProperty
import com.liferay.scalapress.obj.Obj

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

    @BeanProperty var date: Long = _

    @BeanProperty var details: String = _

    @ManyToOne
    @JoinColumn(name = "account", nullable = true)
    @BeanProperty var account: Obj = _

    @Column(name = "orderid", nullable = true)
    @BeanProperty var order: Long = _

    @BeanProperty var paymentType: String = _

    @Column(name = "processorTransactionId")
    @BeanProperty var transactionId: String = _

    @Column(name = "ipAddress")
    @BeanProperty var ipAddress: String = _
}

object Transaction {
    def apply(transactionId: String, orderId: Long, amount: Int) = {
        val payment = new Transaction
        payment.paymentType = "SagePayForm"
        payment.date = System.currentTimeMillis()
        payment.transactionId = transactionId
        payment.order = orderId
        payment.amount = amount
        payment
    }
}