package com.cloudray.scalapress.plugin.calendar

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.search.SearchService
import org.joda.time.{DateMidnight, DateTimeZone}
import com.cloudray.scalapress.ScalapressContext
import scala.beans.BeanProperty
import com.cloudray.scalapress.plugin.friendlyurl.FriendlyUrlGenerator

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
        val result = searchService.search(widget.search)
        result.refs.flatMap(ref => {

            ref.attributes.get(widget.startDateAttribute.id) match {

                case Some(date) if date.forall(_.isDigit) =>

                    val dateMidnight = new DateMidnight(date.toLong, DateTimeZone.UTC)

                    val e = new Event
                    e.date = dateMidnight.getMillis.toString
                    e.dateString = dateMidnight.toString("dd/MM/yyyy")
                    e.title = ref.name
                    e.description = ""
                    e.url = FriendlyUrlGenerator.friendlyUrl(ref.id, ref.name)
                    Some(e)

                case _ => None
            }

        }).toArray
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