package com.cloudray.scalapress.plugin.calendar

import org.joda.time.{PeriodType, Period, DateTimeZone, DateMidnight}
import com.cloudray.scalapress.util.UrlGenerator
import com.cloudray.scalapress.search.{SavedSearch, SearchService}
import com.cloudray.scalapress.item.attr.Attribute
import scala.beans.BeanProperty

/** @author Stephen Samuel */
class CalendarService(searchService: SearchService) {

  def events(search: SavedSearch,
             startDateAttribute: Attribute,
             endDateAttribute: Option[Attribute] = None): Seq[Event] = {

    val result = searchService.search(search)
    result.refs.flatMap(ref => {

      val start = ref.attributes.get(startDateAttribute.id)
        .filter(_.forall(_.isDigit))
        .map(_.toLong)
        .map(timestamp => new DateMidnight(timestamp, DateTimeZone.UTC))

      start match {

        case None => None
        case Some(date) =>

          val end = endDateAttribute
            .map(_.id)
            .flatMap(id => ref.attributes.get(id))
            .filter(_.forall(_.isDigit))
            .map(_.toLong)
            .map(timestamp => new DateMidnight(timestamp, DateTimeZone.UTC))
            .getOrElse(date)

          val lengthOfEventInDays = new Period(date, end, PeriodType.days).getDays + 1
          val dates = for ( day <- 0 until lengthOfEventInDays ) yield date.plusDays(day)

          dates.map(date => {
            val e = new Event
            e.date = date.getMillis.toString
            e.dateString = date.toString("dd/MM/yyyy")
            e.title = ref.name
            e.description = ""
            e.url = UrlGenerator.url(ref)
            e
          })
      }
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