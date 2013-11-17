package com.cloudray.scalapress.plugin.ecommerce.shopping.domain

import javax.persistence.{EnumType, Enumerated, Column, JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Table, Entity}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import com.cloudray.scalapress.theme.Markup
import scala.beans.BeanProperty
import com.cloudray.scalapress.item.StockMethod
import com.cloudray.scalapress.plugin.SingleInstance

/** @author Stephen Samuel */
@Entity
@SingleInstance
@Table(name = "plugins_shopping")
class ShoppingPlugin {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @ManyToOne
  @JoinColumn(name = "basketMarkup")
  @BeanProperty
  var basketMarkup: Markup = _

  @BeanProperty
  var stockMethod: StockMethod = StockMethod.Automatic

  @Column(length = 10000)
  @BeanProperty
  var statuses: String = ShoppingPlugin.defaultStatuses.mkString("\n")

  @Column(length = 100000)
  @BeanProperty
  var outOfStockMessage: String = _

  @Column(length = 100000)
  @BeanProperty
  var orderConfirmationRecipients: String = _

  @Column(length = 100000)
  @BeanProperty
  var orderConfirmationMessageBody: String = _

  @Column(length = 100000)
  @BeanProperty
  var orderCompletionMessageBody: String = _

  @Column(length = 1000)
  @BeanProperty
  var orderConfirmationBcc: String = _

  @Enumerated(EnumType.STRING)
  @BeanProperty
  var checkoutMethod: CheckoutMethod = CheckoutMethod.NO_ACCOUNTS

  @ManyToOne
  @JoinColumn(name = "basketLineMarkup")
  @BeanProperty
  var basketLineMarkup: Markup = _

  @ManyToOne
  @JoinColumn(name = "invoiceLineMarkup")
  @BeanProperty
  var invoiceLineMarkup: Markup = _

  @ManyToOne
  @JoinColumn(name = "invoiceMarkup")
  @BeanProperty
  var invoiceMarkup: Markup = _

  @Column(length = 10000)
  @BeanProperty
  var terms: String = _

  @BeanProperty
  var backorders: Boolean = _

  @Column(length = 10000)
  @BeanProperty
  var termsAcceptance: Boolean = _

  @Column(name = "checkoutScripts", length = 10000)
  @BeanProperty
  var checkoutConfirmationScripts: String = _

  @Column(length = 10000)
  @BeanProperty
  var checkoutConfirmationText: String = _

  @Column
  @BeanProperty
  var emailFormat: String = _
}

object ShoppingPlugin {
  def defaultStatuses: Seq[String] = Seq(Order.STATUS_NEW,
    Order.STATUS_PAID,
    Order.STATUS_COMPLETED,
    Order.STATUS_CANCELLED)
}

trait ShoppingPluginDao extends GenericDao[ShoppingPlugin, java.lang.Long] {
  def get: ShoppingPlugin
}

@Component
@Transactional
class ShoppingPluginDaoImpl extends GenericDaoImpl[ShoppingPlugin, java.lang.Long] with ShoppingPluginDao {
  def get: ShoppingPlugin = findAll.head
}
