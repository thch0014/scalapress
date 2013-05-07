package com.liferay.scalapress.plugin.calendar

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestParam, RequestMapping}
import reflect.BeanProperty
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.search.SearchService
import org.joda.time.{DateTimeZone, DateMidnight, DateTime}
import com.liferay.scalapress.{FriendlyUrlGenerator, ScalapressContext}
import com.liferay.scalapress.obj.attr.AttributeFuncs

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("plugin/calendar/event/"))
class CalendarJsonController {

    @Autowired var searchService: SearchService = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("application/json"), value = Array("widget/{id}"))
    @ResponseBody
    def json(@PathVariable("id") id: Long): Array[Event] = {

        val widget = context.widgetDao.find(id).asInstanceOf[CalendarWidget]
        val results = searchService.search(widget.search)

        val loaded = results.hits.hits.flatMap(hit => {
            val obj = context.objectDao.find(hit.id.toLong)
            AttributeFuncs.attributeValue(obj, widget.startDateAttribute).map(date => (obj, date))
        })

        loaded
          .filter(_._2.forall(_.isDigit))
          .map(arg => {
            val e = new Event
            e.date = new DateMidnight(arg._2.toLong, DateTimeZone.UTC).getMillis.toString
            e.dateString = new DateTime(arg._2.toLong, DateTimeZone.UTC).toString("dd/MM/yyyy")
            e.title = arg._1.name
            e.description = arg._1.summary(200).orNull
            e.url = FriendlyUrlGenerator.friendlyUrl(arg._1)
            e
        })
    }
}

class Event {
    @BeanProperty var date: String = _
    @BeanProperty var dateString: String = _
    @BeanProperty var `type`: String = "meeting"
    @BeanProperty var title: String = _
    @BeanProperty var description: String = _
    @BeanProperty var url: String = _
}