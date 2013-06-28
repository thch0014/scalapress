package com.cloudray.scalapress.search

import com.sksamuel.scoot.soa.Paging
import scala.xml.Utility

/** @author Stephen Samuel */
object PagingRenderer {

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

    def render(paging: Paging): String = render(paging, 5)
    def render(paging: Paging, range: Int): String = {

        val range = 5
        val pages = _renderPages(paging, range)
        val xml = <div class="pagination">
            <ul>
            	<li class="disabled"><i class="icon-search"></i> Viewing results on page </li>
                {pages}
            </ul>
        </div>
        val trimmed = Utility.trim(xml)
        trimmed.toString()
    }
}
