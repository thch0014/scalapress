package com.cloudray.scalapress.search.section

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.section.SectionDao
import org.mockito.Mockito
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class SearchFormSectionControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val controller = new SearchFormSectionController
  controller.context = new ScalapressContext
  controller.context.sectionDao = mock[SectionDao]

  val section = new SearchFormSection

  test("an updated section is persisted") {
    controller.save(section)
    Mockito.verify(controller.context.sectionDao).save(section)
  }
}
