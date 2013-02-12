package com.liferay.scalapress

import com.liferay.scalapress.domain.Folder
import scala.collection.JavaConverters._
import collection.mutable.ArrayBuffer

/** @author Stephen Samuel */
object SectionRenderer {

    def render(folder: Folder, req: ScalapressRequest, context: ScalapressContext): String = {

        val buffer = new ArrayBuffer[String]

        val sorted = folder.sections.asScala.sortBy(_.position)
        val visible = sorted.filter(_.visible)
        for (section <- visible) {
            buffer += "<!-- section " + section.id + ": " + section.getClass + " -->\n"
            val rendered = section.render(req, context)
            if (rendered.isDefined) buffer += (rendered.get + "\n")
            buffer += "<!-- end section -->\n\n"
        }

        buffer.mkString
    }
}
