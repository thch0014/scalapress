package com.cloudray.scalapress.plugin.ecommerce.shopping

import org.springframework.mail.{MailParseException, MailSender, SimpleMailMessage}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.cloudray.scalapress.framework.{Logging, ScalapressContext}
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.ShoppingPluginDao
import javax.mail.internet.AddressException

/** @author Stephen Samuel */
@Component
@Autowired
class OrderCustomerNotificationService(mailSender: MailSender,
                                       context: ScalapressContext,
                                       shoppingPluginDao: ShoppingPluginDao) extends Logging {

  def orderConfirmation(order: Order) {
    Option(shoppingPluginDao.get.orderConfirmationMessageBody) match {
      case None =>
      case Some(body) =>
        val markedup = OrderMarkupService.resolve(order, body)
        _send(order, markedup)
    }
  }

  def orderCompleted(order: Order) {
    Option(shoppingPluginDao.get.orderCompletionMessageBody) match {
      case None =>
      case Some(body) =>
        val markedup = OrderMarkupService.resolve(order, body)
        _send(order, markedup)
    }
  }

  def _format = Option(shoppingPluginDao.get.emailFormat).getOrElse("text")

  def _send(order: Order, body: String) {

    val installation = context.installationDao.get
    val domain = Option(installation.domain).getOrElse("localhost")
    val nowww = if (domain.startsWith("www.")) domain.drop(4) else domain
    val from = "donotreply@" + nowww

    val message = new SimpleMailMessage()
    message.setFrom(from)
    message.setTo(order.account.email)

    val bcc = Option(shoppingPluginDao.get.orderConfirmationBcc).map(_.split(",")).getOrElse(Array[String]())
    if (bcc.size > 0)
      message.setBcc(bcc)

    message.setSubject("Order #" + order.id)
    message.setText(body)

    try {
      mailSender.send(message)
    } catch {
      case e@(_: AddressException | _: MailParseException) =>
        logger.warn(e.toString)
        logger.warn("to: " + order.account.email)
        logger.warn("bcc:" + bcc.mkString(","))
        logger.warn("from: " + from)
      case e: Exception => logger.warn(e.toString)
    }
  }
}
