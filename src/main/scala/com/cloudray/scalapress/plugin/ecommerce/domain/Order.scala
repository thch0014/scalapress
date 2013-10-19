package com.cloudray.scalapress.plugin.ecommerce.domain

import javax.persistence._
import org.joda.time.{DateTime, DateTimeZone}
import scala.beans.BeanProperty
import com.cloudray.scalapress.payments.Transaction
import scala.collection.JavaConverters._
import org.hibernate.annotations._
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.CascadeType
import com.cloudray.scalapress.account.Account

/** @author Stephen Samuel */
@Entity
@Table(name = "orders")
class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: Long = _

  @BeanProperty var status: String = _

  @BeanProperty var deliveryCharge: Int = _
  @BeanProperty var deliveryVatRate: Double = _

  @BeanProperty var customerNote: String = _

  def deliveryEx: Double = deliveryCharge / 100.0
  def deliveryVat: Double = if (vatable) deliveryEx * deliveryVatRate / 100.0 else 0
  def deliveryInc: Double = deliveryEx + deliveryVat

  @Column(name = "deliveryDetails")
  @BeanProperty var deliveryDetails: String = _

  @Column(name = "datePlaced")
  @BeanProperty var datePlaced: Long = _
  def datePlacedLondon(): DateTime = new DateTime(datePlaced, DateTimeZone.forID("Europe/London"))

  @OneToMany(mappedBy = "order", cascade = Array(CascadeType.ALL))
  @BeanProperty var payments: java.util.Set[Transaction] = new java.util.HashSet[Transaction]()

  @OneToMany(mappedBy = "order", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  @Fetch(FetchMode.SUBSELECT)
  @BatchSize(size = 50)
  @BeanProperty var lines: java.util.Set[OrderLine] = new java.util.HashSet[OrderLine]()
  def sortedLines: Seq[OrderLine] = lines.asScala.toSeq.sortBy(_.id)
  def sortedLinesJava = sortedLines.asJava

  @OneToMany(mappedBy = "order", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  @BeanProperty var comments: java.util.Set[OrderComment] = new java.util.HashSet[OrderComment]()
  def sortedComments: Seq[OrderComment] = comments.asScala.toSeq.sortBy(_.id)
  def sortedCommentsJava = sortedComments.asJava

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "deliveryAddress")
  @BeanProperty var deliveryAddress: Address = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "billingAddress")
  @BeanProperty var billingAddress: Address = _

  @BeanProperty var ipAddress: String = _

  @BeanProperty var vatable: Boolean = _

  @BeanProperty var customerReference: String = _

  @BeanProperty var reference: String = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account")
  @BeanProperty var account: Account = _

  @Column(name = "salesPerson")
  @BeanProperty var createdBy: Long = _

  def linesSubtotal: Double = lines.asScala.toSeq.map(_.totalExVat).foldLeft(0.0)((a, b) => a + b)
  def linesVat: Double = if (vatable) lines.asScala.toSeq.map(_.totalVat).foldLeft(0.0)((a, b) => a + b) else 0.0
  def linesTotal: Double = lines.asScala.toSeq.map(_.totalIncVat).foldLeft(0.0)((a, b) => a + b)

  def vat: Double = if (vatable) linesVat + deliveryVat else 0.0
  def subtotal: Double = linesSubtotal + deliveryEx
  def total: Double = ((linesTotal + deliveryInc) * 100).toInt / 100.0
}

object Order {

  val STATUS_NEW = "New"
  val STATUS_PAID = "Paid"
  val STATUS_COMPLETED = "Completed"
  val STATUS_CANCELLED = "Cancelled"

  def apply(ipAddress: String, account: Account) = {
    val order = new Order
    order.datePlaced = new DateTime(DateTimeZone.UTC).getMillis
    order.status = "New"
    order.ipAddress = ipAddress
    order.vatable = true
    order.account = account
    order
  }
}
