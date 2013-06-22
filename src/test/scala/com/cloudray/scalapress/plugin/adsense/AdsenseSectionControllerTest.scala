package com.cloudray.scalapress.plugin.adsense

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.section.SectionDao
import org.mockito.Mockito

/** @author Stephen Samuel */
class AdsenseSectionControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val dao = mock[SectionDao]
    val controller = new AdsenseSectionController
    controller.sectionDao = dao

    test("controller casts to AdsenseSection") {
        val s = new AdsenseSection
        Mockito.when(dao.find(3)).thenReturn(s)
        val actual: AdsenseSection = controller.section(3)
        assert(s === actual)
    }

    test("backoffice url is absolute") {
        assert(new AdsenseSection().backoffice.startsWith("/backoffice"))
    }
}
