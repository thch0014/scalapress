package com.liferay.scalapress.plugin.listings

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.settings.{Installation, InstallationDao}
import org.mockito.Mockito
import com.liferay.scalapress.plugin.listings.domain.ListingPackage
import com.liferay.scalapress.plugin.listings.email.ListingAdminNotificationService

/** @author Stephen Samuel */
class ListingAdminNotificationServiceTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val service = new ListingAdminNotificationService
    service.installationDao = mock[InstallationDao]

    val installation = new Installation
    installation.domain = "coldplay.com"
    Mockito.when(service.installationDao.get).thenReturn(installation)

    val obj = new Obj
    obj.id = 34
    obj.status = "Live"
    obj.name = "coldplay tshirt"

    val lp = new ListingPackage
    lp.name = "t-shirt sale"

    obj.listingPackage = lp

    test("test format of message") {
        val msg = service._message(obj)
        assert(
            "Hello Admin\n\nA new listing has been added to your site:\ncoldplay tshirt\n\n" +
              "The status of this listing is: [Live]\nThe listing was added using: [t-shirt sale]\n\n" +
              "You can edit the listing in the backoffice:\nhttp://coldplay.com/backoffice/obj/34\n\n" +
              "Regards, Scalapress" === msg)
    }

    test("a paid listing should show the paid warning") {
        lp.fee = 100
        val msg = service._message(obj)
        assert(msg.contains("This is a paid listing"))
    }

    test("a free listing should not show the paid warning") {
        lp.fee = 0
        val msg = service._message(obj)
        assert(!msg.contains("This is a paid listing"))
    }
}
