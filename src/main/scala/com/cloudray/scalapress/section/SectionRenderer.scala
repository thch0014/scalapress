package com.cloudray.scalapress.section

import collection.mutable.ArrayBuffer
import com.cloudray.scalapress.{Logging, ScalapressRequest}
import com.cloudray.scalapress.obj.Item
import com.cloudray.scalapress.folder.{SectionInterceptor, Folder}

/** @author Stephen Samuel */
object SectionRenderer extends Logging {

  def render(obj: Item, sreq: ScalapressRequest): String =
    _render(obj.objectType.sortedSections ++ obj.sortedSections, sreq)

  def render(folder: Folder, sreq: ScalapressRequest): String =
    _render(folder.sortedSections, sreq)

  def _render(sections: Seq[Section], sreq: ScalapressRequest): String = {
    val rendered = for ( section <- sections if section.visible ) yield _render(section, sreq)
    rendered.mkString
  }

  def _render(section: Section, sreq: ScalapressRequest): String = {

    val interceptors = sreq.context.beans[SectionInterceptor]

    logger.debug("Rendering section [{}]...", section)

    if (interceptors != null)
      interceptors.foreach(_.preSection(section))

    val start = System.currentTimeMillis()
    val buffer = new ArrayBuffer[String]
    buffer.append("<!-- section " + section.id + ": " + section.getClass + " -->\n")
    try {
      section.render(sreq).foreach(buffer +=)
    } catch {
      case e: Exception => logger.warn("{}", e)
    }
    buffer.append("\n<!-- end section -->\n\n")
    logger.debug("...rendered in {} ms", System.currentTimeMillis() - start)
    val rendered = buffer.mkString

    if (interceptors != null)
      interceptors.foreach(_.postSection(section))

    rendered
  }
}
