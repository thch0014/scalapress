package com.liferay.scalapress

import com.liferay.scalapress.domain.Folder
import scala.collection.JavaConverters._
import collection.mutable.ArrayBuffer

/** @author Stephen Samuel */
object SectionRenderer {

    def render(folder: Folder, req: ScalapressRequest, context: ScalapressContext): String = {

        val buffer = new ArrayBuffer[String]

        val sections = folder.sections
        val sorted = sections.asScala.sortBy(_.position)
        val visible = sorted.filter(_.visible)
        for (section <- visible) {
            buffer += "<!-- section " + section.id + ": " + section.getClass + " -->\n"
            section.render(req, context).foreach(buffer += _)
            buffer += "<!-- end section -->\n\n"
        }

        buffer.mkString
    }
}
