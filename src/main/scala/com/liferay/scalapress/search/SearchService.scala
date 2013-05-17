package com.liferay.scalapress.search

import org.elasticsearch.action.search.SearchResponse
import com.liferay.scalapress.obj.{ObjectType, Obj}

/** @author Stephen Samuel */
trait SearchService {

    def index(obj: Obj)
    def contains(id: String): Boolean
    def search(search: SavedSearch): Seq[ObjectRef]
    def typeahead(q: String, limit: Int): Seq[ObjectRef]

    @deprecated
    def index()
    @deprecated
    def searchType(q: String, t: ObjectType, limit: Int): SearchResponse
}
