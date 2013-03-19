package com.liferay.scalapress.plugin.calendar

import com.liferay.scalapress.ScalapressRequest
import com.liferay.scalapress.widgets.Widget
import javax.persistence.{Table, Entity}

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_calendar_widget")
class CalendarWidget extends Widget {

    def render(req: ScalapressRequest): Option[String] = {
        Some( """<div id="calendar_widget_""" + id + """"></div>
                 <script>
                    $("#calendar_widget_""" + id + """").eventCalendar({ eventsjson: '/plugin/calendar/json' });
                 </script> """)
    }

    //    override def backoffice = "/backoffice/plugin/calendar/widget/" + id
}
