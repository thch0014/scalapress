package com.liferay.scalapress.plugin.ecommerce.domain

import javax.persistence.{JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Column, Table, Entity}
import com.liferay.scalapress.domain.Obj
import reflect.BeanProperty

/** @author Stephen Samuel */
@Table(name = "payments")
@Entity
class Payment {

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

object Payment {
    def apply(transactionId: String, orderId: Long, amount: Int) = {
        val payment = new Payment
        payment.paymentType = "SagePayForm"
        payment.date = System.currentTimeMillis()
        payment.transactionId = transactionId
        payment.order = orderId
        payment.amount = amount
        payment
    }
}