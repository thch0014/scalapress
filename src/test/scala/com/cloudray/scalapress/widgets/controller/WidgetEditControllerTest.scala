package com.cloudray.scalapress.widgets.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import com.cloudray.scalapress.widgets.WidgetDao
import com.cloudray.scalapress.media.MediaWidget

/** @author Stephen Samuel */
class WidgetEditControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val dao = mock[WidgetDao]
  val widgetDao = dao
  val controller = new WidgetEditController(widgetDao)

  test("generic widget controller loads from dao") {
    val w = new MediaWidget
    Mockito.when(dao.find(3)).thenReturn(w)
    val actual = controller.widget(3)
    assert(w === actual)
  }
}
