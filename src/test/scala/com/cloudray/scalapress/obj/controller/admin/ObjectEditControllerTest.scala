package com.cloudray.scalapress.obj.controller.admin

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.attr.{AttributeValueDao, AttributeType}
import com.cloudray.scalapress.obj.{ObjectDao, Obj}
import org.mockito.Mockito
import com.cloudray.scalapress.folder.FolderDao
import com.cloudray.scalapress.section.SectionDao
import javax.servlet.http.HttpServletResponse
import com.cloudray.scalapress.folder.section.{SubfolderSection, FolderContentSection}
import com.cloudray.scalapress.media.{ImageDao, AssetStore}
import com.cloudray.scalapress.search.SearchService
import com.cloudray.scalapress.ScalapressContext
import org.springframework.security.authentication.encoding.PasswordEncoder

/** @author Stephen Samuel */
class ObjectEditControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val objectDao = mock[ObjectDao]
  val folderDao = mock[FolderDao]
  val sectionDao = mock[SectionDao]
  val assetStore = mock[AssetStore]
  val attributeValueDao = mock[AttributeValueDao]
  val imageDao = mock[ImageDao]
  val passwordEncoder = mock[PasswordEncoder]
  val serviceSearch = mock[SearchService]
  val context = new ScalapressContext

  val controller = new ObjectEditController(assetStore,
    attributeValueDao,
    objectDao,
    folderDao,
    imageDao,
    sectionDao,
    context,
    serviceSearch,
    passwordEncoder)

  val response = mock[HttpServletResponse]

  val section1 = new FolderContentSection
  section1.id = 6
  val section2 = new SubfolderSection
  section2.id = 4
  val section3 = new FolderContentSection
  section3.id = 15

  val obj = new Obj
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
    assert(redirect === "redirect:/backoffice/obj/123#tab5")
  }

  it should "section re-ordering persists updated positions" in {
    controller.reorderSections("4-15-6", form, response)
    Mockito.verify(controller.sectionDao).save(section1)
    Mockito.verify(controller.sectionDao).save(section2)
    Mockito.verify(controller.sectionDao).save(section3)
    assert(0 === section2.position)
    assert(1 === section3.position)
    assert(2 === section1.position)
  }
}
