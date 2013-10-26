package com.cloudray.scalapress.obj.attr.widget

import com.cloudray.scalapress.widgets.Widget
import com.cloudray.scalapress.ScalapressRequest
import javax.persistence.{ManyToOne, JoinColumn, Table, Entity}
import scala.beans.BeanProperty
import scala.collection.JavaConverters._
import com.cloudray.scalapress.obj.attr.Attribute

/** @author Stephen Samuel */
@Entity
@Table(name = "widget_attribute_option")
class AttributeWidget extends Widget {

  @ManyToOne
  @JoinColumn(name = "attribute", nullable = true)
  @BeanProperty
  var attribute: Attribute = _

  override def backoffice = "/backoffice/widget/attribute/" + id

  def render(req: ScalapressRequest): Option[String] = {
    Option(attribute) match {
      case None => None
      case Some(c) =>
        val values = attribute.options.asScala.map(option => {
          val link = s"/search?type=${option.attribute.objectType.id}&attr_${option.attribute.id}=${option.value}"
          s"<li>$link</li>"
        })
        Some("<ul class='attribute-widget'>" + values.mkString + "</ul>")
    }
  }
}
