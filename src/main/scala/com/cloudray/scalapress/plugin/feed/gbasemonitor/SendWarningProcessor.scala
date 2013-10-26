package com.cloudray.scalapress.plugin.feed.gbasemonitor

import org.apache.camel.{Exchange, Processor}
import org.springframework.mail.{SimpleMailMessage, MailSender}

/** @author Stephen Samuel */
class SendWarningProcessor(mailSender: MailSender, to: String) extends Processor {

  def process(exchange: Exchange) {

    val result = exchange.getIn.getBody(classOf[GBaseResult])

    val message = new SimpleMailMessage()
    message.setFrom("donotreply@cloudray.co.uk")
    message
      .setText("There was a problem with the latest gbase results, only " + result
      .inserted + " inserts out of " + result.total)
    message.setTo(to)
    message.setSubject("GBase Results Error")

    mailSender.send(message)
  }
}
