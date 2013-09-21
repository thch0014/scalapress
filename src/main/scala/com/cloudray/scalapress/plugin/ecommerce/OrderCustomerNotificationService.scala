package com.cloudray.scalapress.plugin.ecommerce

import domain.Order
import org.springframework.mail.{MailSender, SimpleMailMessage}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.cloudray.scalapress.{ScalapressContext, Logging}

/** @author Stephen Samuel */
@Component
class OrderCustomerNotificationService extends Logging {

  @Autowired var mailSender: MailSender = _
  @Autowired var context: ScalapressContext = _
  @Autowired var shoppingPluginDao: ShoppingPluginDao = _

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

  def _send(order: Order, body: String) {

    val installation = context.installationDao.get
    val domain = Option(installation.domain).getOrElse("localhost")
    val nowww = if (domain.startsWith("www.")) domain.drop(4) else domain
    val replyAddress = "donotreply@" + nowww

    val message = new SimpleMailMessage()
    message.setFrom(s"${installation.name} <$replyAddress>")
    message.setTo(order.account.email)
    message.setReplyTo(replyAddress)
    message.setSubject("Order #" + order.id)
    message.setText(body)
    Option(shoppingPluginDao.get.orderConfirmationBcc).filterNot(_.isEmpty).map(_.split(",")).foreach(message.setBcc)

    try {
      mailSender.send(message)
    } catch {
      case e: Exception => logger.warn(e.toString)
    }
  }
}
