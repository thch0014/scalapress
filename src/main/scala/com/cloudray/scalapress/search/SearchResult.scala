package com.cloudray.scalapress.search

/** @author Stephen Samuel */
case class SearchResult(refs: Seq[ItemRef] = Nil,
                        facets: Seq[Facet] = Nil,
                        count: Int = 0)

case class ItemRef(id: Long,
                   itemType: Long,
                   name: String,
                   status: String,
                   attributes: Map[Long, String],
                   folders: Seq[Long],
                   prioritized: Boolean = false) {
  @deprecated
  def objectType = itemType
}

