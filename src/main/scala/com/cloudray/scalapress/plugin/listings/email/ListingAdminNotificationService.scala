package com.cloudray.scalapress.plugin.listings.email

import com.cloudray.scalapress.settings.InstallationDao
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.{SimpleMailMessage, MailSender}
import com.cloudray.scalapress.obj.Obj
import com.sksamuel.scoot.rest.Logging

/** @author Stephen Samuel
  *
  *         Notifies admin users that a new listing has been added.
  *
  *
  **/
@Component
@Autowired
class ListingAdminNotificationService(mailSender: MailSender,
                                      installationDao: InstallationDao) extends Logging {

  def notify(listing: Obj): Unit = {
    Option(installationDao.get.adminEmail) match {
      case Some(to) =>
        val msg = new SimpleMailMessage
        msg.setTo(to)
        msg.setFrom("donotreply@" + installationDao.get.domain)
        msg.setText(message(listing))
        msg.setSubject("Listing: " + listing.name)
        mailSender.send(msg)
      case _ =>
    }
  }

  private def message(listing: Obj): String = {
    val sb = new StringBuffer("Hello Admin\n\n")
    sb.append("A new listing has been added to your site:\n")
    sb.append(listing.name + "\n\n")

    if (listing.listingPackage.fee > 0) {
      sb.append("** This is a paid listing. You should verify that a payment was been made for this listing **\n\n")
    }

    sb.append("The status of this listing is: [" + listing.status + "]\n")
    sb.append("The listing was added using: [" + listing.listingPackage.name + "]\n\n")
    sb.append("You can edit the listing in the backoffice:\n")
    sb.append("http://" + installationDao.get.domain + "/backoffice/obj/" + listing.id + "\n\n")

    sb.append("Regards, Scalapress")
    sb.toString
  }
}
