package com.liferay.scalapress.plugin.elastic

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.{ObjectType, Obj}
import com.liferay.scalapress.media.Image
import com.liferay.scalapress.enums.{AttributeType, Sort}
import com.liferay.scalapress.obj.attr.{AttributeValue, Attribute}
import com.liferay.scalapress.plugin.elasticsearch.ElasticSearchService
import com.liferay.scalapress.search.SavedSearch

/** @author Stephen Samuel */
class ElasticSearchServiceTest extends FunSuite with MockitoSugar {

    val av1 = new AttributeValue
    av1.attribute = new Attribute
    av1.attribute.id = 1
    av1.value = "mackams"

    val av2 = new AttributeValue
    av2.attribute = av1.attribute
    av2.value = "middlesbrough"

    val av3 = new AttributeValue
    av3.attribute = av1.attribute
    av3.value = "barcodes"

    val av4 = new AttributeValue
    av4.attribute = new Attribute
    av4.attribute.id = 2
    av4.attribute.attributeType = AttributeType.Postcode
    av4.value = "SW10"

    val av5 = new AttributeValue
    av5.attribute = av4.attribute
    av5.value = "SW6"

    val av6 = new AttributeValue
    av6.attribute = av4.attribute
    av6.value = "TS19"

    val obj = new Obj
    obj.id = 2
    obj.name = "tony mowbray"
    obj.objectType = new ObjectType
    obj.objectType.id = 1
    obj.status = "Live"
    obj.images.add(new Image)
    obj.attributeValues.add(av1)
    obj.attributeValues.add(av4)

    val obj2 = new Obj
    obj2.id = 4
    obj2.name = "bryan robson"
    obj2.objectType = new ObjectType
    obj2.objectType.id = 2
    obj2.status = "Disabled"
    obj2.attributeValues.add(av2)
    obj2.attributeValues.add(av5)

    val obj3 = new Obj
    obj3.id = 6
    obj3.name = "steve mclaren"
    obj3.objectType = new ObjectType
    obj3.objectType.id = 3
    obj3.status = "Live"
    obj3.attributeValues.add(av3)
    obj3.attributeValues.add(av6)

    val service = new ElasticSearchService
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

    test("multi name happy path") {

        val search = new SavedSearch
        search.name = "tony mowbray"
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

    test("sorting by attribute value happy path") {

        val search = new SavedSearch
        search.sortType = Sort.Attribute
        search.sortAttribute = av1.attribute

        val results = service.search(search)
        assert(results.hits().hits().size === 3)
        assert(results.hits().hits()(0).id === "6")
        assert(results.hits().hits()(1).id === "2")
        assert(results.hits().hits()(2).id === "4")
    }

    test("sorting by attribute desc value happy path") {

        val search = new SavedSearch
        search.sortType = Sort.AttributeDesc
        search.sortAttribute = av1.attribute

        val results = service.search(search)
        assert(results.hits().hits().size === 3)
        assert(results.hits().hits()(0).id === "4")
        assert(results.hits().hits()(1).id === "2")
        assert(results.hits().hits()(2).id === "6")
    }

    test("distance search happy path") {

        val search = new SavedSearch
        search.location = "SW11"
        search.distance = 100 // only two should be within 100m, TS19 should be 250m away
        search.sortType = Sort.Distance
        assert(service.search(search).hits().hits().size === 2)

        search.distance = 300 // only two should be within 100m, TS19 should be 250m away
        assert(service.search(search).hits().hits().size === 3)
    }

}
