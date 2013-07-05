package com.cloudray.scalapress.section

import collection.mutable.ArrayBuffer
import com.cloudray.scalapress.{Logging, ScalapressContext, ScalapressRequest}
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
object SectionRenderer extends Logging {

    def _render(sections: Seq[Section], req: ScalapressRequest): String = {
        val buffer = new ArrayBuffer[String]
        val sorted = sections.toSeq.sortBy(_.position)
        val visible = sorted.filter(_.visible)
        for ( section <- visible ) {
            buffer += "<!-- section " + section.id + ": " + section.getClass + " -->\n"
            try {
                section.render(req).foreach(buffer += _)
            } catch {
                case e: Exception => logger.warn("{}", e)
            }
            buffer += "\n<!-- end section -->\n\n"
        }

        buffer.mkString
    }

    def render(obj: Obj, req: ScalapressRequest, context: ScalapressContext): String =
        _render(obj.objectType.sortedSections ++ obj.sortedSections, req)

    def render(folder: Folder, req: ScalapressRequest, context: ScalapressContext): String =
        _render(folder.sortedSections, req)
}
