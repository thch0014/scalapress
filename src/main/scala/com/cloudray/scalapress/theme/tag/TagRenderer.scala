package com.cloudray.scalapress.theme.tag

import com.cloudray.scalapress.{Tag, Logging, ScalapressRequest}
import com.cloudray.scalapress.util.ComponentClassScanner

/** @author Stephen Samuel */
object TagRenderer extends Logging {

  lazy val mappings = ComponentClassScanner.tags
    .map(tag => tag.getAnnotation(classOf[Tag]).value() -> tag.newInstance.asInstanceOf[ScalapressTag])
    .toMap ++ EcreatorCompatibleTagMappings.mappings

  def parseQueryString(string: String) =
    string.split("&")
      .map(arg => arg.split("="))
      .map(arg => (arg.head, arg.tail.mkString("=")))
      .toMap

  def regex(tag: String) = "\\[" + tag + "(\\?.*?)?\\]"

  def render(text: String, request: ScalapressRequest): String = {
    Option(text) match {
      case None => ""
      case _ => {

        mappings.foldLeft(text)((b, a) => {
          require(text != null)

          val tagname = a._1
          val tag = a._2

          require(tagname != null)

          regex(tagname).r.replaceAllIn(b, m => {
            require(b != null)

            val params = m.groupCount match {
              case 1 if m.group(1) != null && m.group(1).length > 0 =>
                parseQueryString(m.group(1).drop(1))
              case _ => Map.empty[String, String]
            }

            tag.render(request, params) match {
              case None => ""
              case Some(value) =>
                if (value == null) ""
                else value.replace("$", "\\$")
            }
          })
        })
      }
    }
  }
}
