package com.liferay.scalapress.service.theme.tag

import com.liferay.scalapress.{Logging, ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
object TagRenderer extends Logging {

    val deprecated = Array(
        "comparison_check",
        "distance",
        "attributes_summary_span",
        "location",
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
        "options",
        "invoice_line_options",
        "invoice_voucher_discount")

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

    def render(text: String, request: ScalapressRequest, context: ScalapressContext): String = {
        Option(text) match {
            case None => ""
            case _ => {
                val erased = erase(text)
                TagMappings.mappings.foldLeft(erased)((b, a) => {
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

                        tag.render(request, context, params) match {
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
