package com.liferay.scalapress.widgets

import javax.persistence.{Table, Entity}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.domain.ecommerce.BasketLine
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Table(name = "widgets_basket")
@Entity
class BasketWidget extends Widget {

    def render(req: ScalapressRequest, context: ScalapressContext): Option[String] = {

        val basket = req.basket
        val total = basket.total

        val lines = basket.lines.asScala.map(renderLine(_))

        val xml = <div>Basket Total
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
