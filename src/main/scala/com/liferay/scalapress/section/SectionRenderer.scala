package com.liferay.scalapress.section

import com.liferay.scalapress.domain.{Folder}
import scala.collection.JavaConverters._
import collection.mutable.ArrayBuffer
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.obj.Obj

/** @author Stephen Samuel */
object SectionRenderer {

    def _render(sections: Seq[Section], req: ScalapressRequest): String = {
        val buffer = new ArrayBuffer[String]

        val sorted = sections.toSeq.sortBy(_.position)
        val visible = sorted.filter(_.visible)
        for (section <- visible) {
            buffer += "<!-- section " + section.id + ": " + section.getClass + " -->\n"
            section.render(req, req.context).foreach(buffer += _)
            buffer += "<!-- end section -->\n\n"
        }

        buffer.mkString
    }

    def render(obj: Obj, req: ScalapressRequest, context: ScalapressContext): String =
        _render(obj.sections.asScala.toSeq ++ obj.objectType.sections.asScala.toSeq, req)

    def render(folder: Folder, req: ScalapressRequest, context: ScalapressContext): String =
        _render(folder.sections.asScala.toSeq, req)
}
