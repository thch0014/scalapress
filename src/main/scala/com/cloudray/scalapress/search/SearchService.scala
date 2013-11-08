package com.cloudray.scalapress.search

import com.cloudray.scalapress.item.Item

/** @author Stephen Samuel */
trait SearchService {

  /**
   * Index, or reindex, the given item. In some cases this will cause the given item to be removed from the index,
   * for example if the given item is an invalid state.
   */
  def index(item: Item)

  /**
   * Batch index operation. Implementations can optionally override this to provide faster
   * performance for indexing multiple items at once.
   */
  def index(items: Seq[Item]): Unit = items.foreach(index)

  /**
   * Remove the item with the given id from the index.
   */
  def remove(id: String)

  /**
   * Returns true if the index contains an item with the given id.
   */
  def contains(id: String): Boolean

  /**
   * Returns the total number (or best estimate in the case of distributed search systems)
   * of items in the index.
   */
  def count: Long

  /**
   * Executes a search and returns a SearchResult
   */
  def search(search: Search): SearchResult

  @deprecated
  def typeahead(q: String, limit: Int): Seq[ItemRef]

  /**
   * Returns runtime/debug information on the search system. Is allowed to return Map.empty if no such
   * information is applicable or available.
   */
  def stats: Map[String, String]
}

object SearchService {
}

case class SearchResult(refs: Seq[ItemRef] = Nil, facets: Seq[Facet] = Nil, count: Long = 0)
case class Facet(name: String, field: FacetField, terms: Seq[FacetTerm])
sealed trait FacetField {
  def field: String
}
object FacetField {

  val AttributeFacetFieldRegEx = "attr_facet_(\\d+)".r

  def apply(field: String): FacetField = field match {
    case "tags" => TagsFacetField
    case AttributeFacetFieldRegEx(id) => AttributeFacetField(id.toLong)
    case _ => UnknownFacetField
  }
}
case object UnknownFacetField extends FacetField {
  def field: String = ""
}
case object TagsFacetField extends FacetField {
  def field: String = "tags"
}
case class AttributeFacetField(id: Long) extends FacetField {
  def field: String = "attr_facet_" + id
}
case class FacetTerm(term: String, count: Int)

case class SelectedFacet(field: FacetField, value: String)

case class ItemRef(id: Long,
                   objectType: Long,
                   name: String,
                   status: String,
                   attributes: Map[Long, String],
                   folders: Seq[Long],
                   prioritized: Boolean = false)