package com.cloudray.scalapress.plugin.meta.dublincore

import com.cloudray.scalapress.theme.tag.ScalapressTag
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("dc_publisher")
class DublinCorePublisherTag extends ScalapressTag {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    Some(<meta name="DC.Publisher" content={request.installation.name}/>.toString())
  }
}

@Tag("dc_title")
class DublinCoreTitleTag extends ScalapressTag {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.folder.orElse(request.item).map(meta => {
        <meta name="DC.Title" content={meta.titleTag}/>.toString()
    })
  }
}

@Tag("dc_subject")
class DublinCoreSubjectTag extends ScalapressTag {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.folder.orElse(request.item).map(meta => {
        <meta name="DC.Subject" content={meta.keywordsTag}/>.toString()
    })
  }
}

