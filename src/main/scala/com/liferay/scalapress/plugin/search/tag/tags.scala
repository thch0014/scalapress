package com.liferay.scalapress.plugin.search.tag

import com.liferay.scalapress.service.theme.tag.ScalapressTag
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
object SearchFormTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = None
}

object QuickSearchTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = Some(xml.toString())

    def xml =
        <form method="GET" action="/search">
            <input type="text" name="q" class="search-query" placeholder="Search"/>
            <button>Go</button>
        </form>
}