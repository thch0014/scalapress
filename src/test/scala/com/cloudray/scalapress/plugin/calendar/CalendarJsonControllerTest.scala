package com.cloudray.scalapress.plugin.calendar

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.search._
import org.mockito.{Matchers, Mockito}
import com.cloudray.scalapress.widgets.WidgetDao
import com.cloudray.scalapress.item.attr.Attribute
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.search.ItemRef
import com.cloudray.scalapress.search.SearchResult

/** @author Stephen Samuel */
class CalendarJsonControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val search = mock[SearchService]
  val context = new ScalapressContext
  context.widgetDao = mock[WidgetDao]

  val controller = new CalendarJsonController(search, context)

  val widget = new CalendarWidget
  widget.startDateAttribute = new Attribute
  widget.startDateAttribute.id = 200

  val ref1 = new ItemRef(1, 12, "coldplay", "live", Map(200l -> "1336025005000"), Nil)
  val ref2 = new ItemRef(2, 12, "jethro tull", "live", Map(200l -> "1336053300000"), Nil)
  val ref3 = new ItemRef(3, 13, "keane", "disabled", Map(200l -> "1335660004000"), Nil)

  Mockito.when(context.widgetDao.find(Matchers.anyLong)).thenReturn(widget)

  val result = new SearchResult(Seq(ref1, ref2, ref3), Nil, 0)
  Mockito.when(search.search(Matchers.any[Search])).thenReturn(result)

  test("calendar json happy path") {
    val events = controller.events(4)
    assert(3 === events.size)

    assert("/item-1-coldplay" === events(0).url)
    assert("/item-2-jethro-tull" === events(1).url)
    assert("/item-3-keane" === events(2).url)

    assert("coldplay" === events(0).title)
    assert("jethro tull" === events(1).title)
    assert("keane" === events(2).title)

    assert("03/05/2012" === events(0).dateString)
    assert("03/05/2012" === events(1).dateString)
    assert("29/04/2012" === events(2).dateString)
  }
}
