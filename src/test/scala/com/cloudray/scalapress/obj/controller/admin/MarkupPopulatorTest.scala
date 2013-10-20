package com.cloudray.scalapress.obj.controller.admin

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.theme.{Markup, MarkupDao}

/** @author Stephen Samuel */
class MarkupPopulatorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val m1 = new Markup
    m1.id = 3
    m1.name = "Murdock"

    val m2 = new Markup
    m2.id = 4
    m2.name = "Face man"

    val m3 = new Markup
    m3.id = 2
    m3.name = "hannibal"

    val populator = new MarkupPopulator {
        val markupDao: MarkupDao = mock[MarkupDao]
    }

    Mockito.when(populator.markupDao.findAll()).thenReturn(List(m1, m2, m3))

    test("that themes are populated in order") {
        val model = new ModelMap
        populator.markups(model)
        val themes = model.get("markupMap").asInstanceOf[java.util.Map[String, String]]
        assert(4 === themes.size)
        val it = themes.entrySet().iterator()
        assert("-None-" === it.next().getValue)
        assert("#2 hannibal" === it.next().getValue)
        assert("#3 Murdock" === it.next().getValue)
        assert("#4 Face man" === it.next().getValue)
    }

}
