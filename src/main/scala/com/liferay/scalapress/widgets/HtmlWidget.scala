package com.liferay.scalapress.widgets

import javax.persistence.{Cacheable, Table, Entity}
import com.liferay.scalapress.ScalapressRequest
import org.hibernate.annotations.CacheConcurrencyStrategy

/** @author Stephen Samuel */

@Entity
@Table(name = "boxes_custom")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class HtmlWidget extends Widget {

    override def render(req: ScalapressRequest): Option[String] = {
        Option(content) match {
            case None => None
            case Some(c) =>
                Some(c
                  .replace("src=\"/images/", "src=\"" + req.context.assetStore.baseUrl + "/")
                  .replace("src=\"images/", "src=\"" + req.context.assetStore.baseUrl + "/"))
        }
    }
}
