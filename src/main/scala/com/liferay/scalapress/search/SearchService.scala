package com.liferay.scalapress.search

import org.elasticsearch.action.search.SearchResponse
import com.liferay.scalapress.obj.{ObjectType, Obj}

/** @author Stephen Samuel */
trait SearchService {

    def index()
    def index(obj: Obj)
    def prefix(q: String, limit: Int): SearchResponse
    def search(q: String, limit: Int): SearchResponse
    def search(search: SavedSearch): SearchResponse
    def searchType(q: String, t: ObjectType, limit: Int): SearchResponse
}
