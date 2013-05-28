package com.cloudray.scalapress.section

import collection.mutable.ArrayBuffer
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
object SectionRenderer {

    def _render(sections: Seq[Section], req: ScalapressRequest): String = {
        val buffer = new ArrayBuffer[String]
        val sorted = sections.toSeq.sortBy(_.position)
        val visible = sorted.filter(_.visible)
        for ( section <- visible ) {
            buffer += "<!-- section " + section.id + ": " + section.getClass + " -->\n"
            section.render(req).foreach(buffer += _)
            buffer += "\n<!-- end section -->\n\n"
        }

        buffer.mkString
    }

    def render(obj: Obj, req: ScalapressRequest, context: ScalapressContext): String =
        _render(obj.objectType.sortedSections ++ obj.sortedSections, req)

    def render(folder: Folder, req: ScalapressRequest, context: ScalapressContext): String =
        _render(folder.sortedSections, req)
}
