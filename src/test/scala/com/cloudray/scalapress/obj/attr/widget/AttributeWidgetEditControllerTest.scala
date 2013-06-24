package com.cloudray.scalapress.obj.attr.widget

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import com.cloudray.scalapress.widgets.WidgetDao
import org.springframework.ui.ModelMap

/** @author Stephen Samuel */
class AttributeWidgetEditControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val dao = mock[WidgetDao]
    val controller = new AttributeWidgetEditController
    controller.widgetDao = dao

    test("controller loads instance of AttributeWidget") {
        val w = new AttributeWidget
        Mockito.when(dao.find(3)).thenReturn(w)
        val actual: AttributeWidget = controller.widget(3)
        assert(w === actual)
    }

    test("save persists the widget") {
        val w = new AttributeWidget
        controller.save(w, new ModelMap)
        Mockito.verify(dao).save(w)
    }
}
