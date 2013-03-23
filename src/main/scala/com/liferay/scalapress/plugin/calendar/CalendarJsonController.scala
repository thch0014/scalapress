package com.liferay.scalapress.plugin.calendar

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestParam, RequestMapping}
import reflect.BeanProperty
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.search.SearchService
import org.joda.time.DateTime
import com.liferay.scalapress.{FriendlyUrlGenerator, ScalapressContext}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("plugin/calendar/event/"))
class CalendarJsonController {

    @Autowired var searchService: SearchService = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("application/json"), value = Array("widget/{id}"))
    @ResponseBody
    def json(@RequestParam(value = "objectType", required = false, defaultValue = "0") objectType: Long,
             @PathVariable("id") id: Long): Array[Event] = {

        val widget = context.widgetDao.find(id).asInstanceOf[CalendarWidget]
        val results = searchService.search(widget.search)

        results.hits.hits.map(hit => {

            val obj = context.objectDao.find(hit.id.toLong)

            val e = new Event
            e.date = System.currentTimeMillis.toString
            e.dateString = new DateTime(e.date.toLong).toString("dd/MM/yyyy")
            e.description = obj.name
            e.url = FriendlyUrlGenerator.friendlyUrl(obj)
            e
        })
    }
}

class Event {
    @BeanProperty var date: String = _
    @BeanProperty var dateString: String = _
    @BeanProperty var `type`: String = _
    @BeanProperty var description: String = "meeting"
    @BeanProperty var url: String = _
}