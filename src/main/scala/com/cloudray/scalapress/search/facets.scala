package com.cloudray.scalapress.search

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
  val AttributeFacetFieldRegEx = "attr_facet_(\\d+)".r
  def apply(field: String): Option[FacetField] = field match {
    case "tags" => Some(TagsFacetField)
    case AttributeFacetFieldRegEx(id) => Some(AttributeFacetField(id.toLong))
    case _ => None
  }
}

case object TagsFacetField extends FacetField {
  val key: String = "tags_facet"
}
case class AttributeFacetField(id: Long) extends FacetField {
  val key: String = "attr_facet_" + id
}

/** A facet term is a value in the facet along with the number of occurances.
  */
case class FacetTerm(value: String, count: Int)
case class FacetValue(field: FacetField, value: String)
