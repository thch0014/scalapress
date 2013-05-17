package com.liferay.scalapress.plugin.carousel.slidesjs

import com.liferay.scalapress.section.Section
import javax.persistence._
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import scala.Array
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_slidesjs")
class SlidesJSSection extends Section {

    override def backoffice: String = "/backoffice/plugin/slidesjs/section/" + id

    @BeanProperty var images: Array[String] = Array()

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {

        val renderedImages = images.map(image => {
            val src = context.assetStore.link(image)
            <li>
                <img src={src}/>
            </li>
        })

        val html = <div class="tinycarousel">
            <a class="buttons prev" href="#">left</a>
            <div class="viewport">
                <ul class="overview">
                    {renderedImages}
                </ul>
            </div>
            <a class="buttons next" href="#">right</a>
        </div>

        val script = """<script type="text/javascript">
                             $(document).ready(function() {
                                    $('.tinycarousel').tinycarousel();
                             });
                         </script>"""

        Some(html.toString() + "\n" + script)
    }

    def desc: String = "SlidesJS is a responsive slideshow with CSS3 transitions."
}
