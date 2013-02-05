package com.liferay.scalapress.domain.ecommerce

import javax.persistence.{JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Column, Table, Entity}
import com.liferay.scalapress.domain.{Obj}
import reflect.BeanProperty
import com.liferay.scalapress.enums.PaymentType

/** @author Stephen Samuel */
@Table(name = "payments")
@Entity
class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty
    var id: Long = _

    private var amount: Int = _

    private var authCode: String = _

    private var date: Long = _

    private var details: String = _

    @ManyToOne
    @JoinColumn(name = "account", nullable = true)
    private var account: Obj = _

    private var paymentType: PaymentType = _

    private var description: String = _

    @Column(name = "processorTransactionId")
    private var transactionId: String = _

    @Column(name = "orderid")
    private var order: Long = _

    @Column(name = "ipAddress")
    private var ipAddress: String = _

    private var message: String = _

}

