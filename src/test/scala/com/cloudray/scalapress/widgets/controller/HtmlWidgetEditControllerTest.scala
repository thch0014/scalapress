package com.cloudray.scalapress.widgets.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import com.cloudray.scalapress.widgets.{HtmlWidget, WidgetDao}

/** @author Stephen Samuel */
class HtmlWidgetEditControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val dao = mock[WidgetDao]
    val controller = new WidgetEditController
    controller.widgetDao = dao

    test("controller loads instance of HtmlWidget") {
        val w = new HtmlWidget
        Mockito.when(dao.find(3)).thenReturn(w)
        val actual = controller.widget(3)
        assert(w === actual)
    }
}
