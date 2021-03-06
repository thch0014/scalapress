package com.cloudray.scalapress.theme.tag

import com.cloudray.scalapress.framework.{Logging, ScalapressRequest, Tag, ComponentClassScanner}

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

        try {

          mappings.foldLeft(text)((b, a) => {
            val start = System.currentTimeMillis()
            require(text != null)

            val tagname = a._1
            val tag = a._2

            require(tagname != null)

            val tt = regex(tagname).r.replaceAllIn(b, m => {
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
            val time = System.currentTimeMillis() - start
            if (time > 1)
              logger.debug("Tag {} took {}ms", a._1, time)
            tt
          })

        } catch {
          case e: Exception =>
            logger.error(e.getStackTraceString)
            ""
        }
      }
    }
  }
}
