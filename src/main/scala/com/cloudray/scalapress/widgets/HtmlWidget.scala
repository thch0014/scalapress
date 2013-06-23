package com.cloudray.scalapress.widgets

import javax.persistence.{Table, Entity}
import com.cloudray.scalapress.ScalapressRequest
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "boxes_custom")
class HtmlWidget extends Widget {

    @BeanProperty var simpleEditor: Boolean = false
    override def backoffice = "/backoffice/widget/html/" + id

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
