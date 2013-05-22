package com.liferay.scalapress.plugin.carousel.caroufredsel

import com.liferay.scalapress.section.Section
import javax.persistence._
import com.liferay.scalapress.ScalapressRequest
import scala.beans.BeanProperty
import java.util
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_carousel_caroufredsel")
class CaroufredselSection extends Section {

    override def backoffice: String = "/backoffice/plugin/caroufredsel/section/" + id
    def desc: String = "Caroufredsel is a circular responsive jQuery carousel."

    @ElementCollection
    @BeanProperty var images: java.util.Set[String] = new util.HashSet[String]()

    def render(request: ScalapressRequest): Option[String] = {

        val renderedImages = _images.map(image => {
            val src = request.context.assetStore.link(image)
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

    def _images: Iterable[String] = images.size match {
        case 0 => Option(obj).map(_.images.asScala.map(_.filename)).getOrElse(Nil)
        case _ => images.asScala
    }
}
