package com.liferay.scalapress.domain.ecommerce

import javax.persistence.{Column, Table, Entity, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty
import com.liferay.scalapress.enums.PaymentType

/** @author Stephen Samuel */
@Entity
@Table(name = "orders")
class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty
    var id: Long = _

    var card: Long = _

    /**
     * BC fields
     * should be saleTotalInc, saleTotalEx, and saleTotalVat
     */
    var saleVat: Int = _
    var saleSubtotal: Int = _
    var saleTotal: Int = _

    var paymentCost: Int = _
    var paymentCharge: Int = _

    var deliveryCharge: Int = _
    var deliveryChargeVat: Int = _
    var deliveryChargeInc: Int = _

    var itemDeliveryChargesEx: Int = _
    var datePlaced: Long = _

    var deliveryAddress: Long = _
    var invoiceAddress: Long = _

    var deliveryCode: String = _
    var ipAddress: String = _

    var customerReference: String = _

    var lastExportedOn: Long = _

    var reference: String = _
    var deliveryDetails: String = _
    var ipCountry: String = _
    var deliveryVatRate: Double = _

    var cancelled: Boolean = _

    var lineCount: Int = _

    // customer / account id
    var account: Long = _

    var paymentType: PaymentType = _

    @Column(name = "salesPerson")
    var createdBy: Long = _

    var amountPaid: Int = _

    var vatable: Boolean = _

    var status: String = _

    var authorised: Boolean = _

    var voucherDiscountEx: Int = _
    var voucherDiscountVat: Int = _
    var voucherDiscountInc: Int = _

    var linesInc: Int = _
    var linesVat: Int = _
    var linesEx: Int = _

    var voucherDescription: String = _

    var category: String = _
    var deliveryOption: Long = _

    var vatExemptText: String = _

    var allocated: Boolean = _

    var invoiced: Boolean = _
    var declined: Boolean = _
    var referrer: String = _

    var googleOrderNumber: String = _
    var reason: String = _
    var comment: String = _
    var finansialStatus: String = _
    var payPalTransactionId: String = _
    var cardsaveCrossReference: String = _
}
