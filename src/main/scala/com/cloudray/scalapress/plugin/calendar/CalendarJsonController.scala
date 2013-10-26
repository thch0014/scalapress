package com.cloudray.scalapress.plugin.calendar

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMapping, PathVariable, ResponseBody}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.search.SearchService
import com.cloudray.scalapress.ScalapressContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("plugin/calendar/event/"))
class CalendarJsonController(searchService: SearchService,
                             context: ScalapressContext) {

  val mapper = new ObjectMapper
  mapper.registerModule(DefaultScalaModule)

  @RequestMapping(produces = Array("text/plain"), value = Array("widget/{id}"))
  @ResponseBody
  def json(@PathVariable("id") id: Long): String = {
    val array = events(id)
    mapper.writeValueAsString(array)
  }

  def events(@PathVariable("id") id: Long): Array[Event] = {
    val widget = context.widgetDao.find(id).asInstanceOf[CalendarWidget]
    val calendar = new CalendarService(searchService)
    val events = calendar.events(widget.search, widget.startDateAttribute, Option(widget.endDateAttribute))
    events.toArray
  }
}

