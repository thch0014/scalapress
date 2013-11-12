package com.cloudray.scalapress.search

import com.cloudray.scalapress.item.Item

/** @author Stephen Samuel */
trait SearchService {

  /**
   * Returns true if an item exists with the given id
   */
  def exists(id: String): Boolean

  /**
   * Returns the total number (or best estimate in the case of distributed search systems)
   * of items in the index.
   */
  def count: Long

  /**
   * Executes a search and returns a SearchResult
   */
  def search(search: Search): SearchResult
}

trait SearchStats {
  /**
   * Returns runtime/debug information on the search system. Is allowed to return Map.empty if no such
   * information is applicable or available.
   */
  def stats: Map[String, String]
}

/**
 * A specialization of the SearchService for services that require items to be explicity indexed.
 * For example when using an indexing search like Solr.
 */
trait IndexedSearchService extends SearchService {

  /**
   * Remove the item with the given id from the index.
   */
  def remove(id: String)

  /**
   * Index, or reindex, the given item. In some cases this will cause the given item to be
   * removed from the index, for example if the given item is an invalid state.
   *
   * Some implementations may render this operation as a no-op, if they do not use an indexing step.
   */
  def index(item: Item)

  /**
   * Batch index operation. Implementations can optionally override this to provide faster
   * performance for indexing multiple items at once; otherwise default implementation is to simply
   * call index(item) for each item in the sequence.
   */
  def index(items: Seq[Item]): Unit = items.foreach(index)
}

