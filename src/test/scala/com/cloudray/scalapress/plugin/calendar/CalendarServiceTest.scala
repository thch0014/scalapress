package com.cloudray.scalapress.plugin.calendar

import com.cloudray.scalapress.search.{SearchResult, SavedSearch, ObjectRef, SearchService}
import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.Obj
import org.joda.time.{DateTimeZone, DateTime}
import com.cloudray.scalapress.obj.attr.Attribute
import org.mockito.Mockito
import scala.beans.BeanProperty

/** @author Stephen Samuel */
class CalendarServiceTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val searchService = mock[SearchService]
    val service = new CalendarService(searchService)

    val startAttribute = new Attribute
    startAttribute.id = 4

    val endAttribute = new Attribute
    endAttribute.id = 5

    val ref1 = ObjectRef(1,
        9,
        "coldplay live in paris",
        Obj.STATUS_LIVE,
        Map(4l -> new DateTime(2013, 10, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis.toString),
        Nil)
    val ref2 = ObjectRef(2,
        9,
        "jethro tull live in london",
        Obj.STATUS_LIVE,
        Map(4l -> new DateTime(2013, 10, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis.toString,
            5l -> new DateTime(2013, 10, 12, 0, 0, 0, 0, DateTimeZone.UTC).getMillis.toString), // 3 day event
        Nil)

    val search = new SavedSearch
    val result = SearchResult(Seq(ref1, ref2))
    Mockito.when(searchService.search(search)).thenReturn(result)

    test("events with a start and end date generate unique events for each date period") {
        val events = service.events(search, startAttribute, Some(endAttribute))
        assert(5 === events.size)
        assert(new DateTime(2013, 10, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis === events(0).date.toLong)
        assert(new DateTime(2013, 10, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis === events(1).date.toLong)
        assert(new DateTime(2013, 10, 10, 0, 0, 0, 0, DateTimeZone.UTC).getMillis === events(2).date.toLong)
        assert(new DateTime(2013, 10, 11, 0, 0, 0, 0, DateTimeZone.UTC).getMillis === events(3).date.toLong)
        assert(new DateTime(2013, 10, 12, 0, 0, 0, 0, DateTimeZone.UTC).getMillis === events(4).date.toLong)
    }

    test("when end date is not specified then only start dates should be used") {
        val events = service.events(search, startAttribute, None)
        assert(2 === events.size)
        assert(new DateTime(2013, 10, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis === events(0).date.toLong)
        assert(new DateTime(2013, 10, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis === events(1).date.toLong)
    }
}