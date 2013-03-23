package com.liferay.scalapress.theme

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.plugin.ecommerce.domain.{OrderLine, BasketLine}
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.folder.Folder
import com.liferay.scalapress.service.theme.tag.TagRenderer

/** @author Stephen Samuel */
object MarkupRenderer {

    def render(lines: Seq[BasketLine], m: Markup, r: ScalapressRequest): String = {
        require(m != null)
        val start = TagRenderer.render(m.start, r, r.context)
        val body =
            lines
              .map(l => TagRenderer.render(m.body, r.withLine(l), r.context))
              .mkString(TagRenderer.render(m.between, r, r.context))
        val end = TagRenderer.render(m.end, r, r.context)
        start + body + end
    }

    def render(m: Markup, request: ScalapressRequest): String = render(m, request, request.context)

    @deprecated("use the method that takes only a request")
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

    def renderOrderLines(objects: Seq[OrderLine], m: Markup, request: ScalapressRequest) = {
        val start = TagRenderer.render(m.start, request, request.context)
        val body = objects
          .map(o =>
            TagRenderer.render(m.body, request.withOrderLine(o), request.context))
          .mkString(TagRenderer.render(m.between, request, request.context))
        val end = TagRenderer.render(m.end, request, request.context)
        start + body + end
    }

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