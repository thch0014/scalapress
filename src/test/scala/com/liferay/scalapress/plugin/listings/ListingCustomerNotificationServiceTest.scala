package com.liferay.scalapress.plugin.listings

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.settings.{Installation, InstallationDao}
import org.mockito.Mockito
import com.liferay.scalapress.plugin.listings.domain.ListingPackage
import com.liferay.scalapress.plugin.listings.email.ListingCustomerNotificationService
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
class ListingCustomerNotificationServiceTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val service = new ListingCustomerNotificationService
    service.context = new ScalapressContext
    service.context.installationDao = mock[InstallationDao]

    val installation = new Installation
    installation.domain = "coldplay.com"
    Mockito.when(service.context.installationDao.get).thenReturn(installation)

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
            "Hello.\n\nThank you for submitting a listing to our site.\n\n" +
              "When your listing is approved then you will be able to use the following URL to view it: " +
              "http://coldplay.com/object-34-coldplay-tshirt\n\nIn the meantime, hang tight.\n\nRegards." === msg)
    }
}
