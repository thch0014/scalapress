package com.cloudray.scalapress.widgets

import javax.persistence.{Table, Entity}
import scala.beans.BeanProperty
import com.cloudray.scalapress.framework.ScalapressRequest
import com.cloudray.scalapress.media.ImageResolver

/** @author Stephen Samuel */
@Entity
@Table(name = "boxes_custom")
class HtmlWidget extends Widget {

  @BeanProperty
  var simpleEditor: Boolean = false

  override def backoffice = "/backoffice/widget/html/" + id

  override def render(req: ScalapressRequest): Option[String] = {
    Option(content).map(new ImageResolver(req.context).resolve)
  }
}
