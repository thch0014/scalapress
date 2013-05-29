package com.cloudray.scalapress.plugin.ecommerce

import domain.Order
import org.springframework.mail.{MailSender, SimpleMailMessage}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.cloudray.scalapress.{ScalapressContext, Logging}

/** @author Stephen Samuel */
@Component
class OrderCustomerNotificationService extends Logging {

    val DefaultMessageBody = "Thank you for your order"

    @Autowired var mailSender: MailSender = _
    @Autowired var context: ScalapressContext = _
    @Autowired var shoppingPluginDao: ShoppingPluginDao = _

    def orderPlaced(order: Order) {

        val installation = context.installationDao.get
        val domain = installation.domain
        val nowww = if (domain.startsWith("www.")) domain.drop(4) else domain

        val body = Option(shoppingPluginDao.get.orderConfirmationMessageBody).getOrElse(DefaultMessageBody)
        val markedup = OrderMarkupService.resolve(order, body)

        val message = new SimpleMailMessage()
        message.setFrom(installation.name + " <donotreply@" + nowww + ">")
        message.setTo(order.account.email)
        message.setSubject("Order #" + order.id)
        message.setText(markedup)

        try {
            mailSender.send(message)
        } catch {
            case e: Exception => logger.warn(e.toString)
        }
    }
}
