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
            "Hello Admin\n\nA new listing has been added to your site:\ncoldplay tshirt\n\nThe status of this listing is: [Live]\nThe listing was added using: [t-shirt sale]\n\nYou can edit the listing in the backoffice:\nhttp://coldplay.com/backoffice/obj/34\n\nRegards, Your Server" === msg)
    }
}
