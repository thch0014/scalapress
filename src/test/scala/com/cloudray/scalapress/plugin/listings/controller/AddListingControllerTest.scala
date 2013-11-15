package com.cloudray.scalapress.plugin.listings.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.listings.domain.{ListingsPlugin, ListingPackage, ListingProcess}
import org.mockito.{Matchers, ArgumentCaptor, Mockito}
import com.cloudray.scalapress.plugin.listings._
import com.cloudray.scalapress.item.{ItemType, Item}
import com.cloudray.scalapress.theme.ThemeService
import org.springframework.validation.Errors
import com.cloudray.scalapress.settings.{InstallationDao, Installation}
import com.cloudray.scalapress.folder.{Folder, FolderDao}
import com.cloudray.scalapress.payments._
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import com.cloudray.scalapress.media.{AssetService, AssetStore}
import com.cloudray.scalapress.plugin.vouchers.VoucherDao
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
class AddListingControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val controller = new AddListingController
  controller.paymentCallbackService = mock[PaymentCallbackService]
  controller.listingCallbackProcessor = mock[ListingCallbackProcessor]
  controller.themeService = mock[ThemeService]
  controller.context = new ScalapressContext
  controller.context.paymentPluginDao = mock[PaymentPluginDao]
  controller.listingPackageDao = mock[ListingPackageDao]
  controller.listingsPluginDao = mock[ListingsPluginDao]
  controller.listingProcessDao = mock[ListingProcessDao]
  controller.listingProcessService = mock[ListingProcessService]
  controller.context.paymentPluginDao = mock[PaymentPluginDao]
  controller.context.folderDao = mock[FolderDao]
  controller.paymentFormRenderer = mock[PaymentFormRenderer]
  controller.context.assetStore = mock[AssetStore]
  controller.assetService = mock[AssetService]
  controller.voucherDao = mock[VoucherDao]


  Mockito.when(controller.assetService.upload(Matchers.any[Seq[MultipartFile]])).thenReturn(List("key1", "key2"))

  val plugin = new ListingsPlugin
  Mockito.when(controller.listingsPluginDao.get).thenReturn(plugin)
  Mockito.when(controller.context.paymentPluginDao.enabled).thenReturn(Nil)
  Mockito.when(controller.context.folderDao.tree).thenReturn(Array[Folder]())

  controller.context.installationDao = mock[InstallationDao]
  val installation = new Installation
  installation.domain = "coldplay.com"
  Mockito.when(controller.context.installationDao.get).thenReturn(installation)

  val errors = mock[Errors]

  val req = mock[HttpServletRequest]
  Mockito.when(req.getRequestURL).thenReturn(new StringBuffer("http://domain.com:8080"))
  val process = new ListingProcess
  process.listingPackage = new ListingPackage
  process.listingPackage.objectType = new ItemType
  process.listingPackage.objectType.id = 4
  process.listingPackage.fee = 1000
  process.listing = new Item
  process.listing.id = 454
  process.listing.name = "horse4sale"

  val package1 = new ListingPackage
  package1.name = "gold package"
  val package2 = new ListingPackage
  package2.name = "silver package"
  Mockito.when(controller.listingPackageDao.findAll).thenReturn(List(package1, package2))

  test("a completed listing invokes payment callbacks") {
    controller.completed(process, req)
    Mockito.verify(controller.paymentCallbackService).callbacks(req)
  }

  test("a confirmed process creates a listing and sets this on the process") {

    val listing = new Item
    listing.name = "my super listing"

    Mockito.when(controller.listingProcessService.process(process)).thenReturn(listing)
    controller.confirm(process, errors, req)
    Mockito.verify(controller.listingProcessService).process(process)
    assert(process.listing === listing)
  }

  test("given a package with no fee then confirming the listing also completes it") {
    process.listingPackage.fee = 0
    val listing = new Item
    listing.name = "my super listing"

    Mockito.when(controller.listingProcessService.process(process)).thenReturn(listing)
    controller.confirm(process, errors, req)
    Mockito.verify(controller.listingCallbackProcessor).callback(None, process.listing)
  }

  test("a confirmed process persists process") {
    controller.confirm(process, errors, req)
    Mockito.verify(controller.listingProcessDao).save(process)
  }

  test("a completed listing invokes listing callback if fee is 0") {
    process.listingPackage.fee = 0
    controller.completed(process, req)
    Mockito.verify(controller.listingCallbackProcessor).callback(None, process.listing)
  }

  test("a completed listing cleans up the process session") {
    controller.completed(process, req)
    Mockito.verify(controller.listingProcessService).cleanup(process)
  }

  test("packages page does not show deleted packages") {
    package2.deleted = true
    val page = controller.showPackages(process, errors, req)
    page._body.foreach(body => assert(!body.toString.contains("silver")))
  }

  test("package page contains package text") {
    plugin.packagesPageText = "grandmaster flash loves listing packages"
    val page = controller.showPackages(process, errors, req)
    assert(page._body.filter(_.toString.contains("grandmaster flash loves listing packages")).size > 0)
  }

  test("completed page contains a correct link to the object") {
    val page = controller.completed(process, req)
    assert(page._body.filter(_.toString.contains("coldplay.com/item-454-horse4sale")).size > 0)
  }

  test("selecting package sets package on process") {
    process.listingPackage = null
    Mockito.when(controller.listingPackageDao.find(3)).thenReturn(package1)
    controller.selectPackage(process, errors, 3, req)
    assert(process.listingPackage === package1)
  }

  test("selecting package persists") {
    Mockito.when(controller.listingPackageDao.find(4)).thenReturn(package2)
    controller.selectPackage(process, errors, 4, req)
    Mockito.verify(controller.listingProcessDao).save(process)
  }

  test("folders are automatically set if choice is from 1") {
    assert(0 === process.folders.size)
    process.listingPackage.folders = "5"
    process.listingPackage.maxFolders = 2
    controller.showFolders(process, errors, req)
    assert(1 === process.folders.size)
    assert(5l === process.folders(0))
  }

  test("folders are not automatically set if choice is from more than 1") {
    assert(0 === process.folders.size)
    process.listingPackage.folders = "5,6"
    process.listingPackage.maxFolders = 2
    controller.showFolders(process, errors, req)
    assert(0 === process.folders.size)
  }

  test("select folders uses the http parameter 'folderId'") {
    Mockito.when(req.getParameterValues("folderId")).thenReturn(Array("6", "9", "1"))
    assert(0 === process.folders.size)
    controller.selectFolders(process, errors, req)
    assert(3 === process.folders.size)
    assert(Array(6l, 9l, 1l) === process.folders)
  }

  test("select folders persists process'") {
    Mockito.when(req.getParameterValues("folderId")).thenReturn(Array("6", "9", "1"))
    controller.selectFolders(process, errors, req)
    Mockito.verify(controller.listingProcessDao).save(process)
  }

  test("uploading images adds to existing images'") {

    val bis1 = new ByteArrayInputStream(Array[Byte](1, 2))
    val bis2 = new ByteArrayInputStream(Array[Byte](1, 2))

    process.imageKeys = Array("1.png", "2.png")
    val upload1 = mock[MultipartFile]
    val upload2 = mock[MultipartFile]

    assert(2 === process.imageKeys.size)
    controller.uploadImages(process, Array(upload1, upload2))
    assert(4 === process.imageKeys.size)
    assert(Array("1.png", "2.png", "key1", "key2") === process.imageKeys)
  }

  test("remove key removes image from process'") {

    process.imageKeys = Array("1.png", "2.png")
    assert(2 === process.imageKeys.size)
    controller.removeImage(process, "1.png")
    assert(1 === process.imageKeys.size)
    assert(Array("2.png") === process.imageKeys)
  }

  test("payment page uses purchase with callback of http://domain.com:8080/listing/completed") {
    val plugin1 = new MockPaymentPlugin("superpay", true, Map.empty)
    Mockito.when(controller.context.paymentPluginDao.enabled).thenReturn(Seq(plugin1))
    controller.payments(process, errors, req)
    val captor = ArgumentCaptor.forClass(classOf[Purchase])
    Mockito.verify(controller.paymentFormRenderer).renderPaymentForm(captor.capture)
    assert("http://domain.com:8080/listing/completed" === captor.getValue.successUrl)
  }

  test("folders page uses sections for title") {
    process.listingPackage.folders = "5,6"
    process.listingPackage.maxFolders = 2
    val page = controller.showFolders(process, errors, req)
    assert(page.sreq.title.get === ListingTitles.CHOOSE_FOLDERS)
  }
}
