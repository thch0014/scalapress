package com.liferay.scalapress.plugin.ecommerce

import domain.Order
import com.liferay.scalapress.domain.setup.Installation
import org.springframework.mail.{MailSender, SimpleMailMessage}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.liferay.scalapress.Logging
import collection.mutable.ArrayBuffer

/** @author Stephen Samuel */
@Component
class OrderEmailService extends Logging {

    @Autowired var mailSender: MailSender = _

    def email(bccRecipients: Array[String], order: Order, installation: Installation) {

        val nowww = if (installation.domain.startsWith("www.")) installation.domain.drop(4) else installation.domain
        val body = new ArrayBuffer[String]

        body.append("Thank you for your order")

        val message = new SimpleMailMessage()
        message.setFrom("nodotreply@" + nowww)
        message.setTo(order.account.email)
        if (bccRecipients.size > 0)
            message.setBcc(bccRecipients)
        message.setSubject("Order #" + order.id)
        message.setText(body.mkString("\n"))

        try {
            mailSender.send(message)
        } catch {
            case e: Exception => logger.warn(e.toString)
        }
    }
}