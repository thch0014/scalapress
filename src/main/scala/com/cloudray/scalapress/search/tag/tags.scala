package com.cloudray.scalapress.search.tag

import com.cloudray.scalapress.{Tag, ScalapressRequest}
import scala.collection.JavaConverters._
import com.cloudray.scalapress.obj.attr.AttributeOption
import com.cloudray.scalapress.theme.tag.ScalapressTag
import scala.xml.Utility
import com.cloudray.scalapress.enums.AttributeType

/** @author Stephen Samuel */
object SearchFormTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = None
}

@Tag("quicksearch")
class QuickSearchTag extends ScalapressTag {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

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

@Tag("search_attribute")
class AttributeSearchTag extends ScalapressTag {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    params.get("id") match {
      case None => None
      case Some(id) =>
        val name = "attr_" + id
        Option(request.attribute(id.toLong)).flatMap(attr => {
          attr.attributeType match {
            case AttributeType.Selection =>
              val xml =
                <form method="GET" action="/search">
                  <select name={name} action="/search">
                    {options(attr.options.asScala)}
                  </select>
                  <button type="submit">Go</button>
                </form>
              Some(Utility.trim(xml).toString())
            case _ => None
          }
        })
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

