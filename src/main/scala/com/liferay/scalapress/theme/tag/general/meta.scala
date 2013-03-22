package com.liferay.scalapress.service.theme.tag.general

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}

/** @author Stephen Samuel */
object TitleTagTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        val folderTitle = request.folder.flatMap(f => Option(f.titleTag))
        val objectTitle = request.obj.flatMap(f => Option(f.titleTag))
        val folderName = request.folder.flatMap(f => Option(f.name))
        val objectName = request.obj.flatMap(f => Option(f.name))
        folderTitle.orElse(objectTitle).orElse(folderName).orElse(objectName).map(build(_, params))
    }
}

object KeywordsTagTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        val folderKeywords = request.folder.flatMap(f => Option(f.keywordsTag))
        val objectKeywords = request.obj.flatMap(f => Option(f.keywordsTag))
        folderKeywords.orElse(objectKeywords).map(meta => "<meta name='keywords' content='" + meta + "'/>")
    }
}

object DescriptionTagTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        val folderKeywords = request.folder.flatMap(f => Option(f.descriptionTag))
        val objectKeywords = request.obj.flatMap(f => Option(f.descriptionTag))
        folderKeywords.orElse(objectKeywords).map(meta => "<meta name='description' content='" + meta + "'/>")
    }
}