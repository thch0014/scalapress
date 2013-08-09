package com.cloudray.scalapress.plugin.calendar

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.search.SearchService
import com.cloudray.scalapress.ScalapressContext
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("plugin/calendar/event/"))
class CalendarJsonController {

    @Autowired var searchService: SearchService = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/plain"), value = Array("widget/{id}"))
    @ResponseBody
    def json(@PathVariable("id") id: Long): Array[Event] = {
        val widget = context.widgetDao.find(id).asInstanceOf[CalendarWidget]
        val calendar = new CalendarService(searchService)
        val events = calendar.events(widget.search, widget.startDateAttribute, Option(widget.endDateAttribute))
        events.toArray
    }
}

