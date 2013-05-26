package com.liferay.scalapress.plugin.listings

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.settings.{Installation, InstallationDao}
import org.mockito.{ArgumentCaptor, Mockito}
import com.liferay.scalapress.plugin.listings.domain.ListingPackage
import com.liferay.scalapress.plugin.listings.email.ListingCustomerNotificationService
import com.liferay.scalapress.ScalapressContext
import org.springframework.mail.{MailSender, SimpleMailMessage}

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

    val obj = new Obj
    obj.id = 34
    obj.status = "Live"
    obj.name = "coldplay tshirt"
    obj.account = new Obj
    obj.account.email = "sam@sammy.com"

    val lp = new ListingPackage
    lp.name = "t-shirt sale"

    obj.listingPackage = lp

    test("test format of message") {
        val msg = service._message(obj)
        assert("Hello.\n\nThank you for submitting a listing to our site.\n\n" +
          "When your listing is approved then you will be able to use the following URL to view it: " +
          "http://coldplay.com/object-34-coldplay-tshirt\n\nIn the meantime, hang tight.\n\nRegards." === msg)
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
