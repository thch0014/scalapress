package com.cloudray.scalapress.theme

import com.cloudray.scalapress.ScalapressRequest
import com.cloudray.scalapress.plugin.ecommerce.domain.{OrderLine, BasketLine}
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder
import tag.TagRenderer

/** @author Stephen Samuel */
object MarkupRenderer {

    def render(lines: Seq[BasketLine], m: Markup, r: ScalapressRequest): String = {
        require(m != null)
        val start = TagRenderer.render(m.start, r)
        val body = lines.map(l => TagRenderer.render(m.body, r.withLine(l))).mkString(TagRenderer.render(m.between, r))
        val end = TagRenderer.render(m.end, r)
        start + body + end
    }

    def render(m: Markup, request: ScalapressRequest): String = {
        val start = TagRenderer.render(m.start, request)
        val body = TagRenderer.render(m.body, request)
        val end = TagRenderer.render(m.end, request)
        start + body + end
    }

    def render(f: Folder, m: Markup, request: ScalapressRequest): String = render(m, request.withFolder(f))
    def render(o: Obj, m: Markup, request: ScalapressRequest): String = render(m, request.withObject(o))

    def renderOrderLines(objects: Seq[OrderLine], m: Markup, request: ScalapressRequest) = {
        val start = TagRenderer.render(m.start, request)
        val body = objects.map(o => TagRenderer.render(m.body, request.withOrderLine(o))).mkString(TagRenderer.render(m.between, request))
        val end = TagRenderer.render(m.end, request)
        start + body + end
    }

    def renderObjects(objects: Seq[Obj], m: Markup, request: ScalapressRequest) = {
        val start = TagRenderer.render(m.start, request)
        val body = objects.map(o => TagRenderer.render(m.body, request.withObject(o))).mkString(TagRenderer.render(m.between, request))
        val end = TagRenderer.render(m.end, request)
        start + body + end
    }

    def renderFolders(folders: Seq[Folder], m: Markup, request: ScalapressRequest): String = {
        val start = TagRenderer.render(m.start, request)
        val body = folders.map(f => TagRenderer.render(m.body, request.withFolder(f))).mkString(TagRenderer.render(m.between, request))
        val end = TagRenderer.render(m.end, request)
        start + body + end
    }
}
