package com.liferay.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.obj.{Obj, ObjectType, ObjectDao}
import com.liferay.scalapress.plugin.listings.domain.{ListingProcess, ListingPackage}
import org.mockito.Mockito

/** @author Stephen Samuel */
class ListingProcessServiceTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val process = new ListingProcess
    process.content = "what a lovely mare"
    process.listingPackage = new ListingPackage
    process.listingPackage.objectType = new ObjectType
    process.title = "horse for sale cheap"
    process.accountId = "214"

    val account = new Obj
    account.name = "sammy"

    val service = new ListingProcessService
    service.context = new ScalapressContext
    service.context.objectDao = mock[ObjectDao]
    Mockito.when(service.context.objectDao.find(214)).thenReturn(account)

    test("that the object is assigned the account from the process") {
        val listing = service.process(process)
        listing.account.id = 214
    }

    test("that the object is persisted") {
        val listing = service.process(process)
        Mockito.verify(service.context.objectDao, Mockito.atLeastOnce).save(listing)
    }
}
