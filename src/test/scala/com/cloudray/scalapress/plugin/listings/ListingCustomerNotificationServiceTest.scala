package com.cloudray.scalapress.plugin.listings

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.settings.{Installation, InstallationDao}
import org.mockito.{ArgumentCaptor, Mockito}
import com.cloudray.scalapress.plugin.listings.domain.ListingPackage
import com.cloudray.scalapress.plugin.listings.email.ListingCustomerNotificationService
import com.cloudray.scalapress.ScalapressContext
import org.springframework.mail.{MailSender, SimpleMailMessage}
import com.cloudray.scalapress.account.Account

/** @author Stephen Samuel */
class ListingCustomerNotificationServiceTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val service = new ListingCustomerNotificationService
  service.context = new ScalapressContext
  service.context.installationDao = mock[InstallationDao]
  service.mailSender = mock[MailSender]

  val installation = new Installation
  installation.domain = "coldplay.com"
  installation.name = "coldplay tshirts"
  Mockito.when(service.context.installationDao.get).thenReturn(installation)

  val obj = new Item
  obj.id = 34
  obj.status = "Live"
  obj.name = "coldplay tshirt"
  obj.account = new Account
  obj.account.email = "sam@sammy.com"

  val lp = new ListingPackage
  lp.name = "t-shirt sale"

  obj.listingPackage = lp

  test("message body contains account link") {
    val msg = service._message(obj)
    assert(msg.contains("http://coldplay.com/account"))
  }

  test("message body contains item link") {
    val msg = service._message(obj)
    assert(msg.contains("http://coldplay.com/item-34-coldplay-tshirt"))
  }

  test("email is sent to account email address") {
    service.send(obj)
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    Mockito.verify(service.mailSender).send(captor.capture)
    val msg = captor.getValue
    assert("sam@sammy.com" === msg.getTo.toSeq(0))
  }

  test("email uses listing name as subject") {
    service.send(obj)
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    Mockito.verify(service.mailSender).send(captor.capture)
    val msg = captor.getValue
    assert("Listing: coldplay tshirt" === msg.getSubject)
  }

  test("email uses site name and admin email as from address") {
    service.send(obj)
    val captor = ArgumentCaptor.forClass(classOf[SimpleMailMessage])
    Mockito.verify(service.mailSender).send(captor.capture)
    val msg = captor.getValue
    assert("coldplay tshirts <donotreply@coldplay.com>" === msg.getFrom)
  }
}
