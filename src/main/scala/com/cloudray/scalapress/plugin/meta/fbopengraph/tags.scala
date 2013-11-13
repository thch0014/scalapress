package com.cloudray.scalapress.plugin.meta.fbopengraph

import com.cloudray.scalapress.theme.tag.ScalapressTag
import com.cloudray.scalapress.util.UrlGenerator
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("og_title")
class OpenGraphTitleTag extends ScalapressTag {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.item.map(obj => <meta property="og:title" content={obj.name}/>.toString())
  }
}

@Tag("og_url")
class OpenGraphUrlTag extends ScalapressTag {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.item.map(obj => <meta property="og:url" content={UrlGenerator.url(obj)}/>.toString())
  }
}

@Tag("og_site")
class OpenGraphSiteTag extends ScalapressTag {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    Some(<meta property="og:site_name" content={request.installation.name}/>.toString())
  }
}

@Tag("og_image")
class OpenGraphImageTag extends ScalapressTag {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.item
      .filter(_.images.size > 0)
      .map(obj => {
      val imageLink = "http://" + request.installation.domain + "/images/300/300/" + obj.sortedImages(0)
        <meta property="og:image" content={imageLink}/>.toString()
    })
  }
}

