package com.cloudray.scalapress.plugin.carousel.bootstrapcarousel

import javax.persistence._
import com.cloudray.scalapress.ScalapressRequest
import com.cloudray.scalapress.plugin.carousel.CarouselSection

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_carousel_bootstrapcarousel")
class BootstrapCarouselSection extends CarouselSection {

  override def backoffice: String = "/backoffice/plugin/carousel/bootstrapcarousel/section/" + id
  override def desc: String = "Bootstrap Carousel is the built in bootstrap 3 carousel"

  override def render(request: ScalapressRequest): Option[String] = {

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

    Some("")
  }
}
