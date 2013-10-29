package com.cloudray.scalapress.plugin.listings.email

import org.springframework.mail.{SimpleMailMessage, MailSender}
import com.cloudray.scalapress.{Logging, ScalapressContext}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.cloudray.scalapress.obj.Item
import com.cloudray.scalapress.util.UrlGenerator

/** @author Stephen Samuel */
@Component
class ListingCustomerNotificationService extends Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var mailSender: MailSender = _

    def send(obj: Item) {

        val domain = context.installationDao.get.domain
        val body = _message(obj)

        logger.debug("Emailing new listing [{}]", obj)
        val msg = new SimpleMailMessage
        msg.setTo(obj.account.email)
        msg.setFrom(context.installationDao.get.name + " <donotreply@" + domain + ">")
        msg.setText(body)
        msg.setSubject("Listing: " + obj.name)
        mailSender.send(msg)
    }

    def _message(listing: Item): String = {

        val domain = context.installationDao.get.domain
        val url = "http://" + domain + UrlGenerator.url(listing)
        val accountUrl = "http://" + domain + "/account"

        val sb = new StringBuffer("Hello.\n\n")
        sb.append("Thank you for submitting a listing to our site.\n\n")
        sb.append("When your listing is approved then you will be able to use the following URL to view it: " +
          url + "\n\nYou can also log into your account and update your listing using the following URL: " + accountUrl + "\n\n")
        sb.append("Thanks for reading.")
        sb.toString
    }

}
