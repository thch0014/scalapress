package com.cloudray.scalapress.plugin.ecommerce

import domain.Order
import org.springframework.mail.{MailSender, SimpleMailMessage}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.cloudray.scalapress.{ScalapressContext, Logging}

/** @author Stephen Samuel */
@Component
class OrderAdminNotificationService extends Logging {

    @Autowired var mailSender: MailSender = _
    @Autowired var context: ScalapressContext = _
    @Autowired var shoppingPluginDao: ShoppingPluginDao = _

    def orderConfirmed(order: Order) {
        Option(shoppingPluginDao.get.orderConfirmationRecipients).filterNot(_.isEmpty).map(_.split(Array(' ', ',', '\n'))) match {
            case Some(recipients) if recipients.size > 0 => _send(recipients, order)
            case _ =>
        }
    }

    def _send(recipients: Array[String], order: Order) {

        val installation = context.installationDao.get
        val domain = installation.domain
        val nowww = if (domain.startsWith("www.")) domain.drop(4) else domain

        val message = new SimpleMailMessage()
        message.setFrom(installation.name + " <donotreply@" + nowww + ">")
        message.setTo(recipients)
        message.setSubject("Order #" + order.id + " has been received")
        message
          .setText("A new order has been placed at " + installation
          .name + ".\n\n.Click here to see this order in the backoffice:\nhttp://" + domain + "/backoffice/order/" + order.id)

        try {
            mailSender.send(message)
        } catch {
            case e: Exception => logger.warn(e.toString)
        }
    }
}