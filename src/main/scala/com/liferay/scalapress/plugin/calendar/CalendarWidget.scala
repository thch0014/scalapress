package com.liferay.scalapress.plugin.calendar

import com.liferay.scalapress.ScalapressRequest
import com.liferay.scalapress.widgets.Widget
import javax.persistence.{JoinColumn, ManyToOne, Table, Entity}
import com.liferay.scalapress.search.SavedSearch
import reflect.BeanProperty
import com.liferay.scalapress.obj.ObjectType
import com.liferay.scalapress.obj.attr.Attribute

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_calendar_widget")
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
                    $("#calendar_widget_""" + id + """").eventCalendar({ eventsjson: '/plugin/calendar/event/widget/""" + id + """' });
                 </script> """)
    }

    def search = {
        val search = new SavedSearch
        search.objectType = objectType
        search.status = "Live"
        search
    }

    //    override def backoffice = "/backoffice/plugin/calendar/widget/" + id
}
