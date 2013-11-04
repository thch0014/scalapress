package com.cloudray.scalapress.widgets.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.widgets.{Widget, HtmlWidget, WidgetDao}
import org.mockito.{Matchers, Mockito}
import scala.collection.mutable.ListBuffer
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
class WidgetListControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val context = new ScalapressContext
  context.widgetDao = mock[WidgetDao]
  val controller = new WidgetListController(context)

  val w1 = new HtmlWidget
  w1.id = 6
  val w2 = new HtmlWidget
  w2.id = 4
  val w3 = new HtmlWidget
  w3.id = 15

  val widgets = List(w1, w2, w3)
  Mockito.when(context.widgetDao.findAll).thenReturn(widgets)

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

  test("a created widget is persisted") {
    controller.create(classOf[HtmlWidget].getName)
    Mockito.verify(context.widgetDao).save(Matchers.any[Widget])
  }

  test("delete happy path") {
    controller.delete(3343)
    Mockito.verify(context.widgetDao).removeById(3343)
  }

  import scala.collection.JavaConverters._

  test("widget classes are ordered lexicographically") {
    val map = controller.classes
    val buffer = new ListBuffer[String]
    for ( entry <- map.asScala )
      buffer.append(entry._2)
    assert(buffer.toSeq === buffer.toSeq.sorted)
  }
}
