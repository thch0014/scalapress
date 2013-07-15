package com.cloudray.scalapress.plugin.calendar

import com.cloudray.scalapress.ScalapressRequest
import com.cloudray.scalapress.widgets.Widget
import javax.persistence._
import com.cloudray.scalapress.search.SavedSearch
import com.cloudray.scalapress.obj.ObjectType
import com.cloudray.scalapress.obj.attr.Attribute
import com.cloudray.scalapress.enums.Sort
import scala.beans.BeanProperty

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
    val output = engine.layout("/com/cloudray/scalapress/plugin/calendar/widget.ssp", Map("id" -> id.toString))
    Some(output)
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
