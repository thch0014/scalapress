package com.cloudray.scalapress.plugin.calendar

import com.cloudray.scalapress.widgets.Widget
import javax.persistence._
import com.cloudray.scalapress.search.{Search, Sort}
import com.cloudray.scalapress.item.{Item, ItemType}
import com.cloudray.scalapress.item.attr.Attribute
import scala.beans.BeanProperty
import com.cloudray.scalapress.util.{Page, Scalate}
import com.cloudray.scalapress.framework.ScalapressRequest

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_calendar_widget")
class CalendarWidget extends Widget {

  @ManyToOne
  @JoinColumn(name = "objectType")
  @BeanProperty
  var objectType: ItemType = _

  @ManyToOne
  @JoinColumn(name = "startDateAttribute")
  @BeanProperty
  var startDateAttribute: Attribute = _

  @ManyToOne
  @JoinColumn(name = "endDateAttribute")
  @BeanProperty
  var endDateAttribute: Attribute = _

  def render(req: ScalapressRequest): Option[String] = {
    val output = Scalate.layout("/com/cloudray/scalapress/plugin/calendar/widget.ssp", Map("id" -> id.toString))
    Some(output)
  }

  def search = Search(
    itemTypeId = Option(objectType).map(_.id),
    status = Option(Item.STATUS_LIVE),
    sort = Sort.AttributeDesc,
    sortAttribute = Option(startDateAttribute),
    hasAttributes = List(startDateAttribute.id.toString),
    page = Page(1, 1000)
  )

  override def backoffice = "/backoffice/plugin/calendar/widget/" + id
}
