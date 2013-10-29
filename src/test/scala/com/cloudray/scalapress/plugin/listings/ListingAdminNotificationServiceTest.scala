package com.cloudray.scalapress.plugin.listings

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.Item
import com.cloudray.scalapress.settings.{Installation, InstallationDao}
import org.mockito.{ArgumentCaptor, Matchers, Mockito}
import com.cloudray.scalapress.plugin.listings.domain.ListingPackage
import com.cloudray.scalapress.plugin.listings.email.ListingAdminNotificationService
import org.springframework.mail.{SimpleMailMessage, MailSender}

/** @author Stephen Samuel */
class ListingAdminNotificationServiceTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val installationDao = mock[InstallationDao]
  val sender = mock[MailSender]
  val service = new ListingAdminNotificationService(sender, installationDao)

  val installation = new Installation
  installation.domain = "coldplay.com"
  installation.adminEmail = "sammy@sammy.com"
  Mockito.when(installationDao.get).thenReturn(installation)

  val obj = new Item
  obj.id = 34
  obj.status = "Live"
  obj.name = "coldplay tshirt"

  val lp = new ListingPackage
  lp.name = "t-shirt sale"

  obj.listingPackage = lp

  test("test format of message") {
    service.notify(obj)
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    Mockito.verify(sender).send(captor.capture)
    assert(
      "Hello Admin\n\nA new listing has been added to your site:\ncoldplay tshirt\n\n" +
        "The status of this listing is: [Live]\nThe listing was added using: [t-shirt sale]\n\n" +
        "You can edit the listing in the backoffice:\nhttp://coldplay.com/backoffice/obj/34\n\n" +
        "Regards, Scalapress" === captor.getValue.getText)
  }

  test("a paid listing should show the paid warning") {
    lp.fee = 100
    service.notify(obj)
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    Mockito.verify(sender).send(captor.capture)
    assert(captor.getValue.getText.contains("This is a paid listing"))
  }

  test("a free listing should not show the paid warning") {
    lp.fee = 0
    service.notify(obj)
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    Mockito.verify(sender).send(captor.capture)
    assert(!captor.getValue.getText.contains("This is a paid listing"))
  }

  test("if admin email is not set then no email should be sent") {
    installation.adminEmail = null
    service.notify(obj)
    Mockito.verify(sender, Mockito.never).send(Matchers.any[SimpleMailMessage])
  }
}
