package com.cloudray.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.obj.{Obj, ObjectType, ObjectDao}
import com.cloudray.scalapress.plugin.listings.domain.{ListingProcess, ListingPackage}
import org.mockito.Mockito
import com.cloudray.scalapress.plugin.listings.email.ListingAdminNotificationService
import com.cloudray.scalapress.obj.attr.AttributeValue

/** @author Stephen Samuel */
class ListingProcessServiceTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val process = new ListingProcess
    process.content = "what a lovely mare"
    process.listingPackage = new ListingPackage
    process.listingPackage.objectType = new ObjectType
    process.title = "horse for sale cheap"
    process.accountId = "214"

    val account = new Obj
    account.id = 214
    account.name = "sammy"

    val service = new ListingProcessService
    service.context = new ScalapressContext
    service.context.objectDao = mock[ObjectDao]
    service.listingProcessDao = mock[ListingProcessDao]
    service.listingAdminNotificationService = mock[ListingAdminNotificationService]
    Mockito.when(service.context.objectDao.find(214)).thenReturn(account)

    test("that the object is assigned the account from the process") {
        val listing = service.process(process)
        assert(listing.account.id === 214)
    }

    test("that the object is assigned the listing package from the process") {
        val listing = service.process(process)
        assert(listing.listingPackage === process.listingPackage)
    }



    test("that an admin email is sent") {
        val listing = service.process(process)
        Mockito.verify(service.listingAdminNotificationService).notify(listing)
    }

    test("that the object is persisted") {
        val listing = service.process(process)
        Mockito.verify(service.context.objectDao, Mockito.atLeastOnce).save(listing)
    }

    test("cleanup sets all attribute values to be orphaned") {
        val av1 = new AttributeValue
        av1.listingProcess = process
        val av2 = new AttributeValue
        av2.listingProcess = process
        process.attributeValues.add(av1)
        process.attributeValues.add(av2)
        assert(2 === process.attributeValues.size)
        service.cleanup(process)
        assert(av1.listingProcess == null)
        assert(av2.listingProcess == null)
        assert(0 === process.attributeValues.size)
    }
}
