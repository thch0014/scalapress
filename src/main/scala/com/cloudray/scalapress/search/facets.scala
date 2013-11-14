package com.cloudray.scalapress.search

import com.cloudray.scalapress.framework.ScalapressRequest
import com.cloudray.scalapress.util.UrlParser
import javax.servlet.http.HttpServletRequest
import com.github.theon.uri.Uri
import com.cloudray.scalapress.item.attr.AttributeSelection

/** A facet consists of a logical name, used for human reference, and the field that generates
  * the facet, along with a collection of terms that occur in that facet.
  */
case class Facet(name: String, field: FacetField, terms: Seq[FacetTerm])

/** A FacetField encapsulates which field the facet that is being used was generated from
  */
sealed trait FacetField {
  def key: String
}

object FacetField {
  val AttributeFacetFieldRegEx = "afacet_(\\d+)".r
  def apply(value: String): Option[FacetField] = create.lift(value)
  def create: PartialFunction[String, FacetField] = {
    case "tags" => TagsFacetField
    case AttributeFacetFieldRegEx(id) => AttributeFacetField(id.toLong)
  }
}

case object TagsFacetField extends FacetField {
  val key: String = "tags_facet"
}
case class AttributeFacetField(id: Long) extends FacetField {
  val key: String = "afacet_" + id
}

/** A facet term is a single value in the facet along with the number of occurances.
  */
case class FacetTerm(value: String, count: Int)
case class FacetSelection(field: FacetField, value: String)

object FacetSelections {

  /** Creates a search from a collection of selected facets. The result is a new
    * search with the facet values translated into the appropriate search fields.
    */
  def selections2search(selections: Iterable[FacetSelection]): Search = selections2search(selections, Search.empty)
  def selections2search(selections: Iterable[FacetSelection], search: Search): Search = {
    selections.foldLeft(search)((search, sel) => sel.field match {
      case AttributeFacetField(id) =>
        search.copy(attributeValues = search.attributeValues ++ Seq(AttributeSelection(id.toString, sel.value)))
    })
  }
}

object SearchUrlUtils {

  def sort(sreq: ScalapressRequest): Sort = sort(sreq.request)
  def sort(req: HttpServletRequest): Sort = try {
    Sort.valueOf(UrlParser.parse(req).query.params("sort").headOption.getOrElse("Name"))
  } catch {
    case e: Exception => Sort.Name
  }

  def facets(sreq: ScalapressRequest): Seq[FacetSelection] = facets(sreq.request)
  def facets(req: HttpServletRequest): Seq[FacetSelection] = facets(UrlParser.parse(req))
  def facets(uri: Uri): Seq[FacetSelection] = {
    uri.query.params
      .filter(param => FacetField.create.isDefinedAt(param._1))
      .map(param => FacetSelection(FacetField.create(param._1), param._2.head))
      .toSeq
  }
}
