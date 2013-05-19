package com.liferay.scalapress.theme.tag

import com.liferay.scalapress.{Tag, Logging, ScalapressRequest}
import com.liferay.scalapress.util.ComponentClassScanner

/** @author Stephen Samuel */
object TagRenderer extends Logging {

    val deprecated = Array(
        "comparison_check",
        "attributes_summary_span",
        "favourites",
        "sms",
        "search_begin",
        "search_end",
        "pricing_able2buy",
        "submit",
        "voucher",
        "referafriend",
        "alternatives",
        "gallery_image",
        "sitemap",
        "printer_friendly",
        "member",
        "ordering_qty",
        "ordering_buy",
        "external_links",
        "pricing_discount",
        "newsletter",
        "pricing_original",
        "accessories",
        "options")

    lazy val mappings = ComponentClassScanner.tags
      .map(tag => tag.getAnnotation(classOf[Tag]).value() -> tag.newInstance.asInstanceOf[ScalapressTag])
      .toMap ++ TagMappings.mappings

    def erase(text: String) = {
        require(text != null)
        deprecated.foldLeft(text)((b, a) => b.replaceAll(regex(a), ""))
    }

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
                val erased = erase(text)
                mappings.foldLeft(erased)((b, a) => {
                    require(erased != null)

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
