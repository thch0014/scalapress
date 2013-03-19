package com.liferay.scalapress.plugin.listings.controller.process

import org.springframework.mail.{SimpleMailMessage, MailSender}
import com.liferay.scalapress.ScalapressContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.liferay.scalapress.obj.Obj

/** @author Stephen Samuel */
@Component
class ListingEmailService {

    @Autowired var mailSender: MailSender = _

    def send(obj: Obj, context: ScalapressContext) {
        val msg = new SimpleMailMessage
        msg.setTo(obj.account.email)
        msg.setFrom("donotreply@" + context.installationDao.get.domain)
        msg.setText(message)
        msg.setSubject("Listing: " + obj.name)
        mailSender.send(msg)
    }

    def message: String = {

        val sb = new StringBuffer("Hello.\n\n")
        sb.append("Thank you for submitting a listing to our site.\n\n")
        sb
          .append(
            "Once payment has finished processing then your listing will be visible on our site. We will send you another email once this is completed.\n\nIn the meantime, hang tight.\n\n")
        sb.append("Regards.")
        sb.toString
    }

}
