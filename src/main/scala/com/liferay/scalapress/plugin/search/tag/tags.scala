package com.liferay.scalapress.plugin.search.tag

import com.liferay.scalapress.service.theme.tag.ScalapressTag
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.domain.attr.AttributeOption
import scala.collection.JavaConverters._

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

object AttributeSearchTag extends ScalapressTag {

    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        params.get("id") match {
            case None => None
            case Some(id) =>
                val attribute = context.attributeDao.find(id.toLong)
                val xml =
                    <form method="GET" action="/search">
                        <select name="q" action="/search">
                            {options(attribute.options.asScala)}
                        </select>
                        <button>Go</button>
                    </form>
                Some(xml.toString())
        }
    }

    private def options(options: Seq[AttributeOption]) = {
        options.filter(_.value != null).sortBy(_.value).map(opt => {
            <option>
                {opt.value}
            </option>
        })
    }
}

