package com.liferay.scalapress.widgets

import javax.persistence.{Table, Entity}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */

@Entity
@Table(name = "boxes_custom")
class HtmlWidget extends Widget {

    override def render(req: ScalapressRequest, context: ScalapressContext): Option[String] = Option(content)
}
