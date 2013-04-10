package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.settings.InstallationDao
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.{SimpleMailMessage, MailSender}
import com.liferay.scalapress.obj.Obj
import com.sksamuel.scoot.rest.Logging

/** @author Stephen Samuel */
@Component
class ListingNotificationService extends Logging {

    @Autowired var mailSender: MailSender = _
    @Autowired var installationDao: InstallationDao = _

    def notify(obj: Obj, listingProcess: ListingProcess) {
        val msg = new SimpleMailMessage
        msg.setTo("admin@" + installationDao.get.domain)
        msg.setFrom("donotreply@" + installationDao.get.domain)
        msg.setText(message(obj, listingProcess.listingPackage))
        msg.setSubject("Listing: " + obj.name)
        mailSender.send(msg)
    }

    def message(obj: Obj, listingPackage: ListingPackage): String = {
        val sb = new StringBuffer("Hello Admin\n\n")
        sb.append("A new listing has been added to your site:\n")
        sb.append(obj.name + "\n\n")

        sb.append("The status of this listing is: [" + obj.status + "]\n")
        sb.append("The listing was added using: [" + listingPackage.name + "]\n\n")
        sb.append("You can edit the listing in the backoffice:\n")
        sb.append("http://" + installationDao.get.domain + "/backoffice/obj/" + obj.id + "\n\n")

        sb.append("Regards, Your Server")
        sb.toString
    }
}
