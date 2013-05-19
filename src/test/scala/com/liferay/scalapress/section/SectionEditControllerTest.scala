package com.liferay.scalapress.section

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.springframework.ui.ModelMap
import org.mockito.Mockito
import com.liferay.scalapress.theme.{Markup, MarkupDao}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
class SectionEditControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new SectionEditController
    controller.sectionDao = mock[SectionDao]
    controller.markupDao = mock[MarkupDao]
    val model = new ModelMap

    val section = new StringSection("coldplay")
    Mockito.when(controller.sectionDao.find(123l)).thenReturn(section)

    val markup1 = new Markup
    val markup2 = new Markup
    Mockito.when(controller.markupDao.findAll()).thenReturn(List(markup1, markup2))

    test("controller loads section into model") {
        controller.populate(123l, model)
        assert(section === model.get("section"))
    }

    test("controller loads markups into model as java list") {
        controller.populate(123l, model)
        assert(List(markup1, markup2).asJava === model.get("markups"))
    }
}
