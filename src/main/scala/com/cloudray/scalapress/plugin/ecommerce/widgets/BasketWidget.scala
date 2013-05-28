package com.cloudray.scalapress.plugin.ecommerce.widgets

import javax.persistence.{Cacheable, Table, Entity}
import com.cloudray.scalapress.ScalapressRequest
import scala.collection.JavaConverters._
import com.cloudray.scalapress.widgets.Widget
import com.cloudray.scalapress.plugin.ecommerce.domain.BasketLine
import org.hibernate.annotations.CacheConcurrencyStrategy

/** @author Stephen Samuel */
@Table(name = "widgets_basket")
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class BasketWidget extends Widget {

    def render(req: ScalapressRequest): Option[String] = {

        val basket = req.basket

        val total = basket.total
        val lines = basket.lines.asScala.map(renderLine(_))

        val xml =
            <div>Basket Total
                <div class="basketotal">
                    {total}
                </div>{lines}
            </div>

        Some(xml.toString())
    }

    def renderLine(line: BasketLine) =
        <div class="basketline">
            {line.total}
        </div>
}
