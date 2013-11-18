package com.cloudray.scalapress.item.controller.admin

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.attr.{AttributeValueDao, AttributeType}
import com.cloudray.scalapress.item.{ItemDao, Item}
import org.mockito.Mockito
import com.cloudray.scalapress.folder.FolderDao
import com.cloudray.scalapress.section.SectionDao
import javax.servlet.http.HttpServletResponse
import com.cloudray.scalapress.folder.section.{SubfolderSection, FolderContentSection}
import com.cloudray.scalapress.media.AssetStore
import com.cloudray.scalapress.search.SearchService
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.cloudray.scalapress.account.AccountDao
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.migration.ImageDao

/** @author Stephen Samuel */
class ItemEditControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val objectDao = mock[ItemDao]
  val folderDao = mock[FolderDao]
  val accountDao = mock[AccountDao]
  val sectionDao = mock[SectionDao]
  val assetStore = mock[AssetStore]
  val attributeValueDao = mock[AttributeValueDao]
  val imageDao = mock[ImageDao]
  val passwordEncoder = mock[PasswordEncoder]
  val serviceSearch = mock[SearchService]
  val context = new ScalapressContext

  val controller = new ItemEditController(assetStore,
    attributeValueDao,
    objectDao,
    folderDao,
    imageDao,
    accountDao,
    sectionDao,
    context,
    serviceSearch,
    passwordEncoder)

  val resp = mock[HttpServletResponse]

  val section1 = new FolderContentSection
  section1.id = 6
  val section2 = new SubfolderSection
  section2.id = 4
  val section3 = new FolderContentSection
  section3.id = 15

  val obj = new Item
  obj.sections.add(section1)
  obj.sections.add(section2)
  obj.sections.add(section3)

  val form = new EditForm
  form.o = obj
  form.o.id = 123

  "object controller" should "strip non digits from an attribute Numerical type" in {
    val value = controller._attributeValueNormalize("abc123.4", AttributeType.Numerical)
    assert("123.4" === value)
  }

  it should "return to the images tab when removing an image" in {
    val redirect = controller.removeImage("filename", form)
    assert(redirect === "redirect:/backoffice/item/123#tab5")
  }

  it should "section re-ordering persists updated positions" in {
    controller.reorderSections("4-15-6", form, resp)
    Mockito.verify(controller.sectionDao).save(section1)
    Mockito.verify(controller.sectionDao).save(section2)
    Mockito.verify(controller.sectionDao).save(section3)
    assert(0 === section2.position)
    assert(1 === section3.position)
    assert(2 === section1.position)
  }

  it should "return 200 for section re-ordering" in {
    controller.reorderSections("4-15-6", form, resp)
    Mockito.verify(resp).setStatus(200)
  }

  it should "return 200 for image re-ordering" in {
    controller.reorderImages("4-15-6", form, resp)
    Mockito.verify(resp).setStatus(200)
  }
}
