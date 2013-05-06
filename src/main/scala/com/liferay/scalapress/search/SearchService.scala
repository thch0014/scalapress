package com.liferay.scalapress.search

import org.elasticsearch.action.search.SearchResponse
import com.liferay.scalapress.obj.{ObjectType, Obj}

/** @author Stephen Samuel */
trait SearchService {

    def index(obj: Obj)
    def contains(id: String): Boolean
    def search(search: SavedSearch): SearchResponse

    @deprecated
    def index()
    @deprecated
    def prefix(q: String, limit: Int): SearchResponse
    @deprecated
    def search(q: String, limit: Int): SearchResponse
    @deprecated
    def searchType(q: String, t: ObjectType, limit: Int): SearchResponse
}
