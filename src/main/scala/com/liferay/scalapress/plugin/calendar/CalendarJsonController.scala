package com.liferay.scalapress.plugin.calendar

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestParam, RequestMapping}
import reflect.BeanProperty
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.search.SearchService
import org.joda.time.DateTime

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("plugin/calendar/json"))
class CalendarJsonController {

    @Autowired var searchService: SearchService = _

    @RequestMapping(produces = Array("application/json"))
    @ResponseBody
    def json(@RequestParam(value = "objectType",
        required = false,
        defaultValue = "0") objectType: Long): Array[Event] = {

        val testEvent = new Event
        testEvent.date = System.currentTimeMillis.toString
        testEvent.dateString = new DateTime(testEvent.date).toString("dd/MM/yyyy")
        testEvent.description = "Some sample description"
        testEvent.url = "/obj/12345"
        Array(testEvent)
    }
}

class Event {
    @BeanProperty var date: String = _
    @BeanProperty var dateString: String = _
    @BeanProperty var `type`: String = _
    @BeanProperty var description: String = "meeting"
    @BeanProperty var url: String = _
}