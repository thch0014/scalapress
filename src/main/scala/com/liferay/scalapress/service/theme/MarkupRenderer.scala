package com.liferay.scalapress.service.theme

import tag.TagRenderer
import com.liferay.scalapress.domain.{Obj, Folder, Markup}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
object MarkupRenderer {

    def render(m: Markup, request: ScalapressRequest, context: ScalapressContext): String = {

        val start = TagRenderer.render(m.start, request, context)
        val body = TagRenderer.render(m.body, request, context)
        val end = TagRenderer.render(m.end, request, context)

        start + body + end
    }

    def render(f: Folder, m: Markup, request: ScalapressRequest, context: ScalapressContext): String =
        render(m, request.withFolder(f), context)

    def render(o: Obj, m: Markup, request: ScalapressRequest, context: ScalapressContext): String =
        render(m, request.withObject(o), context)

    def renderObjects(objects: Seq[Obj], m: Markup, request: ScalapressRequest, context: ScalapressContext) = {
        val start = TagRenderer.render(m.start, request, context)
        val body = objects.map(o => TagRenderer.render(m.body, request.withObject(o), context))
          .mkString(TagRenderer.render(m.between, request, context))
        val end = TagRenderer.render(m.end, request, context)
        start + body + end
    }

    def renderFolders(folders: Seq[Folder], m: Markup, request: ScalapressRequest, context: ScalapressContext) = {
        val start = TagRenderer.render(m.start, request, context)
        val body = folders.map(f => TagRenderer.render(m.body, request.withFolder(f), context))
          .mkString(TagRenderer.render(m.between, request, context))
        val end = TagRenderer.render(m.end, request, context)
        start + body + end
    }
}
