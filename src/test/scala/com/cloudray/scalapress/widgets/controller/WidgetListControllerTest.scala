package com.cloudray.scalapress.widgets.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.widgets.{HtmlWidget, WidgetDao}
import org.mockito.Mockito

/** @author Stephen Samuel */
class WidgetListControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new WidgetListController
    controller.widgetDao = mock[WidgetDao]

    val w1 = new HtmlWidget
    w1.id = 6
    val w2 = new HtmlWidget
    w2.id = 4
    val w3 = new HtmlWidget
    w3.id = 15

    val widgets = List(w1, w2, w3)
    Mockito.when(controller.widgetDao.findAll()).thenReturn(widgets)

    test("widgets re-ordering returns ok") {
        val result = controller.reorderWidgets("1,2,3")
        assert("ok" === result)
    }

    test("widgets are reordered if cardinality of ids is equiv to cardinality of widgets") {
        controller.reorderWidgets("15-4-6")
        assert(2 === w1.position)
        assert(1 === w2.position)
        assert(0 === w3.position)
    }

    test("widgets are not reordered if cardinality of ids is different to cardinality of widgets") {
        controller.reorderWidgets("")
        assert(0 === w1.position)
        assert(0 === w2.position)
        assert(0 === w3.position)

        controller.reorderWidgets("4-15")
        assert(0 === w1.position)
        assert(0 === w2.position)
        assert(0 === w3.position)

        controller.reorderWidgets("4-15-6-7")
        assert(0 === w1.position)
        assert(0 === w2.position)
        assert(0 === w3.position)
    }
}
