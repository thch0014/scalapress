package com.cloudray.scalapress.widgets

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{Cookie, HttpServletRequest}
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import org.mockito.Mockito
import com.cloudray.scalapress.media.AssetStore

/** @author Stephen Samuel */
class WidgetsTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  context.widgetDao = mock[WidgetDao]
  context.assetStore = mock[AssetStore]

  val widget1 = new HtmlWidget
  widget1.location = "right"
  widget1.visible = true
  widget1.content = "stringer"

  val widget2 = new HtmlWidget
  widget2.location = "right"
  widget2.visible = true
  widget2.content = "bunk"

  val widget3 = new HtmlWidget
  widget3.location = "upside"
  widget3.visible = true
  widget3.content = "mcnulty"

  Mockito.when(context.widgetDao.findAll()).thenReturn(List(widget1, widget2, widget3))

  val tag = new WidgetsTag()
  val sreq = ScalapressRequest(req, context)

  test("no location returns none") {
    val actual = tag.render(sreq, Map.empty)
    assert(None === actual)
  }

  test("tag uses sep param") {
    val actual = tag.render(sreq, Map("sep" -> "!m!55!!", "location" -> "right"))
    assert(actual.get.contains("!m!55!!"))
  }

  test("tag only renders correct location") {
    val actual = tag.render(sreq, Map("sep" -> "!", "location" -> "upside"))
    assert(
      "<!-- widget: class com.cloudray.scalapress.widgets.HtmlWidget-->\n<table cellspacing='0' cellpadding='0' id='widget0' class=' widgetcontainer'><caption></caption><tr><td>mcnulty</td></tr></table>\n<!-- end widget -->"
        .trim === actual.get.trim)
  }

  test("widget tag adds one time cookies to request") {
    widget1.oneTimeVisible = true
    widget1.location = "left"
    widget1.id = 5
    widget2.oneTimeVisible = true
    widget2.location = "left"
    widget2.id = 8
    Mockito.when(req.getCookies).thenReturn(Array[Cookie]())
    tag.render(sreq, Map("location" -> "left"))
    //   assert(sreq.outgoingCookies.size === 2)
    //   assert(sreq.outgoingCookies.apply(0).getName == "widgetseen_5")
    //  assert(sreq.outgoingCookies.apply(1).getName == "widgetseen_8")
  }
}
