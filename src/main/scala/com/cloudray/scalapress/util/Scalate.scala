package com.cloudray.scalapress.util

import org.fusesource.scalate.TemplateEngine
import org.fusesource.scalate.support.StringTemplateSource
import org.apache.commons.io.IOUtils

/** @author Stephen Samuel */
object Scalate {
  val engine = new TemplateEngine

  def layout(uri: String): String = layout(uri, Map.empty)
  def layout(uri: String, map: Map[String, Any]): String = engine.layout(uri, map)
  def layout(uri: String, loader: ClassLoader, map: Map[String, Object]): String = {
    val string = IOUtils.toString(loader.getResourceAsStream(uri))
    val source = new StringTemplateSource(uri, string)
    engine.layout(source, map)
  }
}
