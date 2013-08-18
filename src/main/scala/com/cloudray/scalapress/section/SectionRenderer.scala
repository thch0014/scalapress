package com.cloudray.scalapress.section

import collection.mutable.ArrayBuffer
import com.cloudray.scalapress.{Logging, ScalapressRequest}
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
object SectionRenderer extends Logging {

  def _render(sections: Seq[Section], req: ScalapressRequest): String = {

    val visible = sections.filter(_.visible)

    val buffer = new ArrayBuffer[String]
    for ( section <- visible ) {
      logger.debug("rendering section {}", section.getClass)
      buffer += "<!-- section " + section.id + ": " + section.getClass + " -->\n"
      try {
        section.render(req).foreach(buffer +=)
      } catch {
        case e: Exception => logger.warn("{}", e)
      }
      buffer += "\n<!-- end section -->\n\n"
      logger.debug("section completed")
    }

    buffer.mkString
  }

  def render(obj: Obj, req: ScalapressRequest): String =
    _render(obj.objectType.sortedSections ++ obj.sortedSections, req)

  def render(folder: Folder, req: ScalapressRequest): String = _render(folder.sortedSections, req)
}
