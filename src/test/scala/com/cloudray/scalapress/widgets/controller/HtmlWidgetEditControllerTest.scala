package com.cloudray.scalapress.widgets.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import com.cloudray.scalapress.widgets.{HtmlWidget, WidgetDao}
import org.springframework.ui.ModelMap

/** @author Stephen Samuel */
class HtmlWidgetEditControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val dao = mock[WidgetDao]
    val controller = new HtmlWidgetEditController
    controller.widgetDao = dao

    test("controller loads instance of HtmlWidget") {
        val w = new HtmlWidget
        Mockito.when(dao.find(3)).thenReturn(w)
        val actual: HtmlWidget = controller.widget(3)
        assert(w === actual)
    }

    test("save persists the widget") {
        val w = new HtmlWidget
        controller.save(w, new ModelMap)
        Mockito.verify(dao).save(w)
    }
}
