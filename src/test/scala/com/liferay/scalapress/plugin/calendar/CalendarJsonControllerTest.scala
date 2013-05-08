package com.liferay.scalapress.plugin.calendar

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.search.{SavedSearch, ObjectRef, SearchService}
import com.liferay.scalapress.ScalapressContext
import org.mockito.{Matchers, Mockito}
import com.liferay.scalapress.widgets.WidgetDao
import com.liferay.scalapress.obj.attr.Attribute

/** @author Stephen Samuel */
class CalendarJsonControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new CalendarJsonController
    val search = mock[SearchService]
    val context = new ScalapressContext
    context.widgetDao = mock[WidgetDao]

    controller.context = context
    controller.searchService = search

    val widget = new CalendarWidget
    widget.startDateAttribute = new Attribute
    widget.startDateAttribute.id = 200

    val ref1 = new ObjectRef(1, 12, "coldplay", "live", Map(200l -> "1336025005000"), Nil)
    val ref2 = new ObjectRef(2, 12, "jethro tull", "live", Map(200l -> "1336053300000"), Nil)
    val ref3 = new ObjectRef(3, 13, "keane", "disabled", Map(200l -> "1335660004000"), Nil)

    Mockito.when(context.widgetDao.find(Matchers.anyLong)).thenReturn(widget)
    Mockito.when(search.search(Matchers.any[SavedSearch])).thenReturn(Seq(ref1, ref2, ref3))

    test("calendar json happy path") {
        val events = controller.json(4)
        assert(3 === events.size)

        assert("/object-1-coldplay" === events(0).url)
        assert("/object-2-jethro-tull" === events(1).url)
        assert("/object-3-keane" === events(2).url)

        assert("coldplay" === events(0).title)
        assert("jethro tull" === events(1).title)
        assert("keane" === events(2).title)

        assert("03/05/2012" === events(0).dateString)
        assert("03/05/2012" === events(1).dateString)
        assert("29/04/2012" === events(2).dateString)
    }
}