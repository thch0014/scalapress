package com.liferay.scalapress.search

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.{ObjectType, Obj}
import com.liferay.scalapress.media.Image
import com.liferay.scalapress.enums.Sort

/** @author Stephen Samuel */
class ElasticSearchServiceTest extends FunSuite with MockitoSugar {

    val service = new ElasticSearchService
    val obj = new Obj
    obj.id = 2
    obj.name = "tony mowbray"
    obj.objectType = new ObjectType
    obj.objectType.id = 1
    obj.status = "Live"
    obj.images.add(new Image)

    val obj2 = new Obj
    obj2.id = 4
    obj2.name = "bryan robson"
    obj2.objectType = new ObjectType
    obj2.objectType.id = 2
    obj2.status = "Disabled"


    val obj3 = new Obj
    obj3.id = 6
    obj3.name = "steve mclaren"
    obj3.objectType = new ObjectType
    obj3.objectType.id = 3
    obj3.status = "Live"

    service.index(obj2)
    service.index(obj3)
    service.index(obj)

    Thread.sleep(1000)

    test("indexing and retrieval by name happy path") {

        val search = new SavedSearch
        search.name = "tony"
        val results = service.search(search)
        assert(results.hits().hits().size === 1)
        assert(results.hits().hits()(0).id === "2")
    }

    test("indexing and retrieval by object type happy path") {

        val search = new SavedSearch
        search.objectType = obj2.objectType
        val results = service.search(search)
        assert(results.hits().hits().size === 1)
        assert(results.hits().hits()(0).id === "4")
    }

    test("indexing and retrieval by only images happy path") {

        val search = new SavedSearch
        search.imageOnly = true
        val results = service.search(search)
        assert(results.hits().hits().size === 1)
        assert(results.hits().hits()(0).id === "2")
    }

    test("sorting by name happy path") {

        val search = new SavedSearch
        search.sortType = Sort.Name
        val results = service.search(search)
        assert(results.hits().hits().size === 3)
        assert(results.hits().hits()(0).id === "4")
        assert(results.hits().hits()(1).id === "6")
        assert(results.hits().hits()(2).id === "2")
    }

    test("sorting by Newest happy path") {

        val search = new SavedSearch
        search.sortType = Sort.Newest
        val results = service.search(search)
        assert(results.hits().hits().size === 3)
        assert(results.hits().hits()(0).id === "6")
        assert(results.hits().hits()(1).id === "4")
        assert(results.hits().hits()(2).id === "2")
    }

    test("sorting by Oldest happy path") {

        val search = new SavedSearch
        search.sortType = Sort.Oldest
        val results = service.search(search)
        assert(results.hits().hits().size === 3)
        assert(results.hits().hits()(0).id === "2")
        assert(results.hits().hits()(1).id === "4")
        assert(results.hits().hits()(2).id === "6")
    }
}
