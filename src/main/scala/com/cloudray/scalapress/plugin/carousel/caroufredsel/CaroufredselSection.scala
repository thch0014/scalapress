package com.cloudray.scalapress.plugin.carousel.caroufredsel

import javax.persistence._
import com.cloudray.scalapress.ScalapressRequest
import com.cloudray.scalapress.plugin.carousel.CarouselSection

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_carousel_caroufredsel")
class CaroufredselSection extends CarouselSection {

    override def backoffice: String = "/backoffice/plugin/carousel/caroufredsel/section/" + id
    def desc: String = "Caroufredsel is a circular responsive jQuery carousel."

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

}
