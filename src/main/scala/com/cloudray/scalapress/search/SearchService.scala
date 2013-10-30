package com.cloudray.scalapress.search

import com.cloudray.scalapress.item.Item

/** @author Stephen Samuel */
trait SearchService {

  /**
   * Index, or reindex, the given item. In some cases this will cause the given item to be removed from the index,
   * for example if the given item is in deleted state.
   */
  def index(obj: Item)

  /**
   * Batch index operation. Is included for implementations that provide speed increases on batch operations,
   * but by default will simply invoke index multiple times for the contents of the sequence.
   */
  def index(objs: Seq[Item]): Unit = objs.foreach(index)

  /**
   * Remove the entry with the given id from the index
   */
  def remove(id: String)

  /**
   * Returns true if the index contains an entry with the given id.
   */
  def contains(id: String): Boolean

  /**
   * Returns the total number (or best estimate in the case of distributed search systems) of entries in the index.
   */
  def count: Long

  /**
   * Perform a search.
   */
  def search(search: SavedSearch): SearchResult

  @deprecated
  def typeahead(q: String, limit: Int): Seq[ItemRef]

  /**
   * Returns runtime/debug information on the search system. Is allowed to return Map.empty if no such
   * information is applicable or available.
   */
  def stats: Map[String, String]
}

object SearchService {
  val FACET_TAGS = "tags"
}

case class SearchResult(refs: Seq[ItemRef] = Nil, facets: Seq[Facet] = Nil, count: Long = 0)
case class Facet(name: String, field: String, terms: Seq[FacetTerm])
case class FacetTerm(term: String, count: Int)
case class ItemRef(id: Long,
                   objectType: Long,
                   name: String,
                   status: String,
                   attributes: Map[Long, String],
                   folders: Seq[Long],
                   prioritized: Boolean = false)