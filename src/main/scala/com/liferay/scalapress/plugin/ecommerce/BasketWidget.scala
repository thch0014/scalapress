package com.liferay.scalapress.plugin.ecommerce

import domain.BasketLine
import javax.persistence.{Table, Entity}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import scala.collection.JavaConverters._
import com.liferay.scalapress.widgets.Widget

/** @author Stephen Samuel */
@Table(name = "widgets_basket")
@Entity
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
