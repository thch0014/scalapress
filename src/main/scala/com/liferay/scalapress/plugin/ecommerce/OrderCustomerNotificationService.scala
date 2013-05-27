package com.liferay.scalapress.plugin.ecommerce

import domain.Order
import org.springframework.mail.{MailSender, SimpleMailMessage}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.liferay.scalapress.{ScalapressContext, Logging}
import collection.mutable.ArrayBuffer

/** @author Stephen Samuel */
@Component
class OrderCustomerNotificationService extends Logging {

    @Autowired var mailSender: MailSender = _
    @Autowired var context: ScalapressContext = _

    def orderPlaced(order: Order) {

        val installation = context.installationDao.get
        val domain = installation.domain
        val nowww = if (domain.startsWith("www.")) domain.drop(4) else domain
        val body = new ArrayBuffer[String]

        body.append("Thank you for your order")

        val message = new SimpleMailMessage()
        message.setFrom(installation.name + " <donotreply@" + nowww + ">")
        message.setTo(order.account.email)
        message.setSubject("Order #" + order.id)
        message.setText(body.mkString("\n"))

        try {
            mailSender.send(message)
        } catch {
            case e: Exception => logger.warn(e.toString)
        }
    }

    def orderCompleted(order: Order) {

        val domain = context.installationDao.get.domain
        val nowww = if (domain.startsWith("www.")) domain.drop(4) else domain
        val body = new ArrayBuffer[String]

        body.append("Thank you for your order")

        val message = new SimpleMailMessage()
        message.setFrom("nodotreply@" + nowww)
        message.setTo(order.account.email)
        message.setSubject("Order #" + order.id)
        message.setText(body.mkString("\n"))

        try {
            mailSender.send(message)
        } catch {
            case e: Exception => logger.warn(e.toString)
        }
    }
}
