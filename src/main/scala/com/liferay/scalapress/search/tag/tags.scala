package com.liferay.scalapress.search.tag

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import scala.collection.JavaConverters._
import com.liferay.scalapress.obj.attr.AttributeOption
import com.liferay.scalapress.theme.tag.ScalapressTag

/** @author Stephen Samuel */
object SearchFormTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = None
}

object QuickSearchTag extends ScalapressTag {

    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        val placeholder = params.get("placeholder").orElse(params.get("initial")).getOrElse("")
        val objectType = params
          .get("objectType")
          .map(arg => <input type="hidden" value={arg.toString} name="objectType"/>).orNull

        Some(<form method="GET" action="/search">
            {objectType}<input type="text" name="q" class="search-query" placeholder={placeholder}/>
            <button>Go</button>
        </form>.toString())
    }
}

object AttributeSearchTag extends ScalapressTag {

    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        params.get(" id ") match {
            case None => None
            case Some(id) =>
                val attribute = context.attributeDao.find(id.toLong)
                val xml =
                    <form method=" GET " action="/search">
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

