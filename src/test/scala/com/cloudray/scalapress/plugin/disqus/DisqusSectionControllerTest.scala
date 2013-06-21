package com.cloudray.scalapress.plugin.disqus

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.section.SectionDao
import org.mockito.Mockito

/** @author Stephen Samuel */
class DisqusSectionControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val dao = mock[SectionDao]
    val controller = new DisqusSectionController
    controller.sectionDao = dao

    test("discus controller casts to DiscusSection") {
        val disqus = new DisqusSection
        Mockito.when(dao.find(3)).thenReturn(disqus)
        val actual: DisqusSection = controller.section(3)
        assert(disqus === actual)
    }
}
