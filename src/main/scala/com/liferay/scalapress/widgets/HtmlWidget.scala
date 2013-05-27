package com.liferay.scalapress.widgets

import javax.persistence.{Table, Entity}
import com.liferay.scalapress.ScalapressRequest

/** @author Stephen Samuel */

@Entity
@Table(name = "boxes_custom")
class HtmlWidget extends Widget {

    override def render(req: ScalapressRequest): Option[String] = {
        Option(content) match {
            case None => None
            case Some(c) =>
                val replaced = c
                  .replace("src=\"/images/", "src=\"" + req.context.assetStore.baseUrl + "/")
                  .replace("src=\"images/", "src=\"" + req.context.assetStore.baseUrl + "/")
                Some(replaced)
        }
    }
}
