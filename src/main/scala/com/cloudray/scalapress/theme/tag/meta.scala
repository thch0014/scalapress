package com.cloudray.scalapress.theme.tag

import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("meta_title")
class TitleTagTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    val folderTitle = request.folder.flatMap(f => Option(f.titleTag))
    val objectTitle = request.item.flatMap(f => Option(f.titleTag))
    val folderName = request.folder.flatMap(f => Option(f.name))
    val objectName = request.item.flatMap(f => Option(f.name))
    folderTitle.orElse(objectTitle).orElse(folderName).orElse(objectName).map(build(_, params))
  }
}

@Tag("meta_keywords")
class KeywordsTagTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    val folderKeywords = request.folder.flatMap(f => Option(f.keywordsTag))
    val objectKeywords = request.item.flatMap(f => Option(f.keywordsTag))
    folderKeywords.orElse(objectKeywords).map(meta => "<meta name='keywords' content='" + meta + "'/>")
  }
}

@Tag("meta_description")
class DescriptionTagTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    val folderKeywords = request.folder.flatMap(f => Option(f.descriptionTag))
    val objectKeywords = request.item.flatMap(f => Option(f.descriptionTag))
    folderKeywords.orElse(objectKeywords).map(meta => "<meta name='description' content='" + meta + "'/>")
  }
}