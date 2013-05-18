package com.liferay.scalapress.plugin.search.elastic

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.{ObjectType, Obj}
import com.liferay.scalapress.media.Image
import com.liferay.scalapress.enums.{AttributeType, Sort}
import com.liferay.scalapress.obj.attr.{AttributeDao, AttributeValue, Attribute}
import com.liferay.scalapress.search.{SearchService, SavedSearch}
import com.liferay.scalapress.ScalapressContext
import org.mockito.Mockito
import scala.collection.JavaConverters._
import com.liferay.scalapress.plugin.search.elasticsearch.ElasticSearchService

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

    val av7 = new AttributeValue
    av7.attribute = new Attribute
    av7.attribute.id = 62
    av7.value = "attribute with space"

    val obj = new Obj
    obj.id = 2
    obj.name = "tony mowbray"
    obj.objectType = new ObjectType
    obj.objectType.id = 1
    obj.status = "Live"
    obj.images.add(new Image)
    obj.attributeValues.add(av1)
    obj.attributeValues.add(av4)
    obj.attributeValues.add(av7)
    obj.labels = "coldplay,jethro tull"

    val obj2 = new Obj
    obj2.id = 4
    obj2.name = "bryan robson"
    obj2.objectType = new ObjectType
    obj2.objectType.id = 2
    obj2.status = "Disabled"
    obj2.attributeValues.add(av2)
    obj2.attributeValues.add(av5)
    obj2.labels = "coldplay"

    val obj3 = new Obj
    obj3.id = 20
    obj3.name = "steve mclaren"
    obj3.objectType = new ObjectType
    obj3.objectType.id = 3
    obj3.status = "Live"
    obj3.attributeValues.add(av3)
    obj3.attributeValues.add(av6)

    val service = new ElasticSearchService
    service.context = new ScalapressContext
    service.context.attributeDao = mock[AttributeDao]
    Mockito.when(service.context.attributeDao.findAll()).thenReturn(List(av1.attribute, av4.attribute, av7.attribute))

    service.setupIndex()
    service.index(obj2)
    service.index(obj3)
    service.index(obj)

    Thread.sleep(1200)

    test("indexing and retrieval by name happy path") {

        val search = new SavedSearch
        search.name = "tony"
        val results = service.search(search).refs
        assert(results.size === 1)
        assert(results(0).id === 2)
    }

    test("multi name happy path") {

        val search = new SavedSearch
        search.name = "tony mowbray"
        val results = service.search(search).refs
        assert(results.size === 1)
        assert(results(0).id === 2)
    }

    test("indexing and retrieval by object type happy path") {

        val search = new SavedSearch
        search.objectType = obj2.objectType
        val results = service.search(search).refs
        assert(results.size === 1)
        assert(results(0).id === 4)
    }

    test("indexing and retrieval by only images happy path") {

        val search = new SavedSearch
        search.imageOnly = true
        val results = service.search(search).refs
        assert(results.size === 1)
        assert(results(0).id === 2)
    }

    test("sorting by name happy path") {

        val search = new SavedSearch
        search.sortType = Sort.Name
        val results = service.search(search).refs
        assert(results.size === 3)
        assert(results(0).id === 4)
        assert(results(1).id === 20)
        assert(results(2).id === 2)
    }

    test("sorting by Newest happy path") {

        val search = new SavedSearch
        search.sortType = Sort.Newest
        val results = service.search(search).refs
        assert(results.size === 3)
        assert(results(0).id === 20)
        assert(results(1).id === 4)
        assert(results(2).id === 2)
    }

    test("sorting by Oldest happy path") {

        val search = new SavedSearch
        search.sortType = Sort.Oldest
        val results = service.search(search).refs
        assert(results.size === 3)
        assert(results(0).id === 2)
        assert(results(1).id === 4)
        assert(results(2).id === 20)
    }

    test("sorting by attribute value happy path") {

        val search = new SavedSearch
        search.sortType = Sort.Attribute
        search.sortAttribute = av1.attribute

        val results = service.search(search).refs
        assert(results.size === 3)
        assert(results(0).id === 20)
        assert(results(1).id === 2)
        assert(results(2).id === 4)
    }

    test("sorting by attribute desc value happy path") {

        val search = new SavedSearch
        search.sortType = Sort.AttributeDesc
        search.sortAttribute = av1.attribute

        val results = service.search(search).refs
        assert(results.size === 3)
        assert(results(0).id === 4)
        assert(results(1).id === 2)
        assert(results(2).id === 20)
    }

    test("distance search happy path") {

        val search = new SavedSearch
        search.location = "SW11"
        search.distance = 100 // only two should be within 100m, TS19 should be 250m away
        search.sortType = Sort.Distance
        assert(service.search(search).refs.size === 2)

        search.distance = 300 // only two should be within 100m, TS19 should be 250m away
        assert(service.search(search).refs.size === 3)
    }

    test("resp2ref happy path") {

        val search = new SavedSearch
        search.keywords = "mowbray"

        val results = service.search(search).refs
        assert(results.size === 1)
        assert(2 === results(0).id)
        assert("tony mowbray" === results(0).name)
        assert("Live" === results(0).status)
        assert(3 === results(0).attributes.size)
    }

    test("attribute search with spaces") {

        val searchav = new AttributeValue
        searchav.attribute = new Attribute
        searchav.attribute.id = 62
        searchav.value = "attribute with space"


        val search = new SavedSearch
        search.attributeValues = Set(searchav).asJava

        val results = service.search(search).refs
        assert(results.size === 1)
        assert(2 === results(0).id)
        assert("tony mowbray" === results(0).name)
    }

    test("wildcard search count brings back total count") {

        val search = new SavedSearch
        val count = service.search(search).count
        assert(3 === count)
    }

    test("name search returns query based count") {
        val search = new SavedSearch
        search.name = "mowbray"
        val count = service.search(search).count
        assert(1 === count)
    }

    test("attribute search returns query based count") {

        val searchav = new AttributeValue
        searchav.attribute = new Attribute
        searchav.attribute.id = 62
        searchav.value = "attribute with space"

        val search = new SavedSearch
        search.attributeValues.add(searchav)

        val count = service.search(search).count
        assert(1 === count)
    }

    test("wildcard search getting tag facets") {

        val search = new SavedSearch
        search.facets = Seq(SearchService.FACET_TAGS)
        val result = service.search(search)

        assert(1 === result.facets.size)
        assert(SearchService.FACET_TAGS === result.facets.head.name)
        assert(2 === result.facets.head.terms.find(_.term == "coldplay").get.count)
        assert(1 === result.facets.head.terms.find(_.term == "jethro tull").get.count)
    }
}
