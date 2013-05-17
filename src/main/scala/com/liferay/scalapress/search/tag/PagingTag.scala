package com.liferay.scalapress.search.tag

import com.liferay.scalapress.theme.tag.ScalapressTag
import com.liferay.scalapress.{Tag, ScalapressContext, ScalapressRequest}
import com.sksamuel.scoot.soa.Paging

/** @author Stephen Samuel */
@Tag("pagination")
class PagingTag extends ScalapressTag {

    def _renderPages(paging: Paging) = {
        paging.range(5).map(p => {
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

                val pages = _renderPages(paging)
                val pagination = <div class="pagination">
                    <ul>
                        {pages}
                    </ul>
                </div>
                Some(pagination.toString())
        }
    }
}
