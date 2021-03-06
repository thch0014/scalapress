package com.cloudray.scalapress.search.section

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.section.SectionDao
import org.mockito.Mockito
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
class SearchResultsSectionControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val context = new ScalapressContext
  context.sectionDao = mock[SectionDao]

  val markupDao = mock[MarkupDao]
  val controller = new SearchResultsSectionController(markupDao, context)

  val section = new SearchResultsSection

  test("an updated section is persisted") {
    controller.save(section)
    Mockito.verify(context.sectionDao).save(section)
  }
}
