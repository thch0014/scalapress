package com.liferay.scalapress

import com.liferay.scalapress.domain.Folder
import scala.collection.JavaConverters._
import collection.mutable.ArrayBuffer

/** @author Stephen Samuel */
object PluginRenderer {

    def render(folder: Folder, req: ScalapressRequest, context: ScalapressContext): String = {

        val buffer = new ArrayBuffer[String]

        val sections = folder.sections.asScala.sortBy(_.position)
        val visible = sections.filter(_.visible)
        for (section <- visible) {
            buffer += "<!-- section: " + section.getClass + " -->\n"
            val rendered = section.render(req, context)
            if (rendered.isDefined) buffer += (rendered.get + "\n")
            buffer += "<!-- end section -->\n\n"
        }

        buffer.mkString
    }
}
