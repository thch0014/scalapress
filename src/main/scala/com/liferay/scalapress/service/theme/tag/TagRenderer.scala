package com.liferay.scalapress.service.theme.tag

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
object TagRenderer {

    val deprecated = Array("comparison_check",
        "distance",
        "attributes_summary_span",
        "location",
        "favourites",
        "sms",
        "alternatives",
        "gallery_image", "sitemap", "printer_friendly")

    def erase(text: String) = {
        require(text != null)
        deprecated.foldLeft(text)((b, a) => b.replaceAll(regex(a), ""))
    }

    def parseQueryString(string: String) = string.split("&").map(arg => arg.split("=")).filter(_.size == 2)
      .map(arg => (arg(0), arg(1))).toMap

    def regex(tag: String) = "\\[" + tag + "(\\?.*?)?\\]"

    def render(text: String, request: ScalapressRequest, context: ScalapressContext): String = {
        Option(text) match {
            case None => ""
            case _ => {
                val erased = erase(text)
                TagMappings.mappings.foldLeft(erased)((b, a) => {

                    val tagname = a._1
                    val tag = a._2

                    regex(tagname).r.replaceAllIn(b, m => {
                        val params = Option(m.group(1)) match {
                            case Some(query) if query.length > 0 => parseQueryString(m.group(1).drop(1))
                            case _ => Map.empty[String, String]
                        }
                        tag.render(request, context, params).getOrElse("")
                    })
                })
            }
        }
    }
}
