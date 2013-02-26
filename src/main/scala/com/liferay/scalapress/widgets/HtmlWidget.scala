package com.liferay.scalapress.widgets

import javax.persistence.{Table, Entity}
import com.liferay.scalapress.ScalapressRequest
import org.hibernate.annotations.CacheConcurrencyStrategy

/** @author Stephen Samuel */

@Entity
@Table(name = "boxes_custom")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
class HtmlWidget extends Widget {

    override def render(req: ScalapressRequest): Option[String] = {
        Option(content) match {
            case None => None
            case Some(c) =>
                Some(c
                  .replace("src=\"/images/", "src=\"" + req.context.assetStore.cdn + "/")
                  .replace("src=\"images/", "src=\"" + req.context.assetStore.cdn + "/"))
        }
    }
}
