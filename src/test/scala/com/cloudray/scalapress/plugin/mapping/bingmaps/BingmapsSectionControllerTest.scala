package com.cloudray.scalapress.plugin.mapping.bingmaps

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.section.SectionDao
import org.mockito.Mockito

/** @author Stephen Samuel */
class BingmapsSectionControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val dao = mock[SectionDao]
  val controller = new BingMapSectionController(dao)

  test("controller casts to BingMapSection") {
    val s = new BingMapSection
    Mockito.when(dao.find(3)).thenReturn(s)
    val actual: BingMapSection = controller.section(3)
    assert(s === actual)
  }

  test("backoffice url is absolute") {
    assert(new BingMapSection().backoffice.startsWith("/backoffice"))
  }
}
