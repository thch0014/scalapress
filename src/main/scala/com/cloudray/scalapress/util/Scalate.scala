package com.cloudray.scalapress.util

import org.fusesource.scalate.TemplateEngine

/** @author Stephen Samuel */
object Scalate {
  val engine = new TemplateEngine
  def layout(source: String) = layout(source, Map.empty)
  def layout(source: String, map: Map[String, Any]) = engine.layout(source, map)
}
