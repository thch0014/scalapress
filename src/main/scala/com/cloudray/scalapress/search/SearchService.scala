package com.cloudray.scalapress.search

import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
trait SearchService {

    def index(obj: Obj)
    def remove(id: String)
    def contains(id: String): Boolean
    def count: Long
    def search(search: SavedSearch): SearchResult
    def typeahead(q: String, limit: Int): Seq[ObjectRef]
    def stats: Map[String, String]

    @deprecated def index()
}

object SearchService {
    val FACET_TAGS = "tags"
}

case class SearchResult(refs: Seq[ObjectRef] = Nil, facets: Seq[Facet] = Nil, count: Int = 0)
case class Facet(name: String, terms: Iterable[FacetTerm])
case class FacetTerm(term: String, count: Int)
case class ObjectRef(id: Long,
                     objectType: Long,
                     name: String,
                     status: String,
                     attributes: Map[Long, String],
                     folders: Seq[Long])