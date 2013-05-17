package com.liferay.scalapress.search.tag

import com.liferay.scalapress.theme.tag.ScalapressTag
import com.liferay.scalapress.{Tag, ScalapressContext, ScalapressRequest}
import com.sksamuel.scoot.soa.Paging
import scala.xml.Utility

/** @author Stephen Samuel */
@Tag("pagination")
class PagingTag extends ScalapressTag {

    def _renderPages(paging: Paging, range: Int) = {
        paging.range(range).map(p => {
            val url = paging.url(p)
            <li>
                <a href={url}>
                    {p}
                </a>
            </li>
        })
    }

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        request.paging match {
            case None => None
            case Some(paging) =>

                val range = params.get("range").getOrElse("5").toInt
                val pages = _renderPages(paging, range)
                val xml = <div class="pagination">
                    <ul>
                        {pages}
                    </ul>
                </div>
                val trimmed = Utility.trim(xml)
                Some(trimmed.toString())
        }
    }
}
