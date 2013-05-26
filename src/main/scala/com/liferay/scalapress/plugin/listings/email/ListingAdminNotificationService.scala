package com.liferay.scalapress.plugin.listings.email

import com.liferay.scalapress.settings.InstallationDao
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.{SimpleMailMessage, MailSender}
import com.liferay.scalapress.obj.Obj
import com.sksamuel.scoot.rest.Logging

/** @author Stephen Samuel
  *
  *         Notifies admin users that a new listing has been added.
  *
  *
  * */
@Component
class ListingAdminNotificationService extends Logging {

    @Autowired var mailSender: MailSender = _
    @Autowired var installationDao: InstallationDao = _

    def notify(listing: Obj) {
        val msg = new SimpleMailMessage
        msg.setTo(installationDao.get.adminEmail)
        msg.setFrom("donotreply@" + installationDao.get.domain)
        msg.setText(_message(listing))
        msg.setSubject("Listing: " + listing.name)
        mailSender.send(msg)
    }

    def _message(listing: Obj): String = {
        val sb = new StringBuffer("Hello Admin\n\n")
        sb.append("A new listing has been added to your site:\n")
        sb.append(listing.name + "\n\n")

        sb.append("The status of this listing is: [" + listing.status + "]\n")
        sb.append("The listing was added using: [" + listing.listingPackage.name + "]\n\n")
        sb.append("You can edit the listing in the backoffice:\n")
        sb.append("http://" + installationDao.get.domain + "/backoffice/obj/" + listing.id + "\n\n")

        sb.append("Regards, Your Server")
        sb.toString
    }
}
