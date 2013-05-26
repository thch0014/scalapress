package com.liferay.scalapress.plugin.listings.email

import org.springframework.mail.{SimpleMailMessage, MailSender}
import com.liferay.scalapress.{Logging, ScalapressContext}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.plugin.friendlyurl.FriendlyUrlGenerator

/** @author Stephen Samuel */
@Component
class ListingCustomerNotificationService extends Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var mailSender: MailSender = _

    def send(obj: Obj) {

        val domain = context.installationDao.get.domain
        val body = _message(obj)

        logger.info("Emailing new listing [{}]", obj)
        val msg = new SimpleMailMessage
        msg.setTo(obj.account.email)
        msg.setFrom(context.installationDao.get.name + " <donotreply@" + domain + ">")
        msg.setText(body)
        msg.setSubject("Listing: " + obj.name)
        mailSender.send(msg)
    }

    def _message(listing: Obj): String = {

        val domain = context.installationDao.get.domain
        val url = "http://" + domain + FriendlyUrlGenerator.friendlyUrl(listing)

        val sb = new StringBuffer("Hello.\n\n")
        sb.append("Thank you for submitting a listing to our site.\n\n")
        sb.append("When your listing is approved then you will be able to use the following URL to view it: " +
          url + "\n\nIn the meantime, hang tight.\n\n")
        sb.append("Regards.")
        sb.toString
    }

}
