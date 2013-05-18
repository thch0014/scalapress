package com.liferay.scalapress.search

import com.liferay.scalapress.obj.{ObjectType, Obj}
import org.elasticsearch.action.search.SearchResponse

/** @author Stephen Samuel */
trait SearchService {

    def index(obj: Obj)
    def contains(id: String): Boolean
    def count: Long
    def count(search: SavedSearch): Long
    def search(search: SavedSearch): Seq[ObjectRef]
    def typeahead(q: String, limit: Int): SearchResult

    @deprecated def index()
    @deprecated def searchType(q: String, t: ObjectType, limit: Int): SearchResponse
}

case class SearchResult(refs: Seq[ObjectRef] = Nil, facets: Seq[Facet] = Nil)
case class Facet(name: String, values: Seq[FacetTerm])
case class FacetTerm(term: String, count: Int)
case class ObjectRef(id: Long,
                     objectType: Long,
                     name: String,
                     status: String,
                     attributes: Map[Long, String],
                     folders: Seq[Long])