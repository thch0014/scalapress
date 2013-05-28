package com.cloudray.scalapress.plugin.calendar

import com.cloudray.scalapress.ScalapressRequest
import com.cloudray.scalapress.widgets.Widget
import javax.persistence._
import com.cloudray.scalapress.search.SavedSearch
import com.cloudray.scalapress.obj.ObjectType
import com.cloudray.scalapress.obj.attr.Attribute
import com.cloudray.scalapress.enums.Sort
import org.hibernate.annotations.CacheConcurrencyStrategy
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_calendar_widget")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class CalendarWidget extends Widget {

    @ManyToOne
    @JoinColumn(name = "objectType")
    @BeanProperty var objectType: ObjectType = _

    @ManyToOne
    @JoinColumn(name = "startDateAttribute")
    @BeanProperty var startDateAttribute: Attribute = _

    @ManyToOne
    @JoinColumn(name = "endDateAttribute")
    @BeanProperty var endDateAttribute: Attribute = _

    def render(req: ScalapressRequest): Option[String] = {
        Some( """<div id="calendar_widget_""" + id + """"></div>
                 <script>
                    $("#calendar_widget_""" + id + """").eventCalendar({  moveSpeed: 200, showDescription: true, eventsScrollable: true, eventsjson: '/plugin/calendar/event/widget/""" + id + """' });
                 </script> """)
    }

    def search = {
        val search = new SavedSearch
        search.objectType = objectType
        search.status = "Live"
        search.maxResults = 1000
        search.sortType = Sort.AttributeDesc
        search.sortAttribute = startDateAttribute
        search.hasAttributes = startDateAttribute.id.toString
        search
    }

    override def backoffice = "/backoffice/plugin/calendar/widget/" + id
}
