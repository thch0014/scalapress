package com.cloudray.scalapress.plugin.search.elastic

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.{ObjectType, Obj}
import com.cloudray.scalapress.media.Image
import com.cloudray.scalapress.enums.{AttributeType, Sort}
import com.cloudray.scalapress.obj.attr.{AttributeDao, AttributeValue, Attribute}
import com.cloudray.scalapress.search.{SearchService, SavedSearch}
import com.cloudray.scalapress.ScalapressContext
import org.mockito.Mockito
import scala.collection.JavaConverters._
import com.cloudray.scalapress.plugin.search.elasticsearch.ElasticSearchService

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

    val date1 = new AttributeValue
    date1.attribute = new Attribute
    date1.attribute.attributeType = AttributeType.Date
    date1.attribute.id = 345
    date1.value = "51454"

    val date2 = new AttributeValue
    date2.attribute = date1.attribute
    date2.value = "3156777"

    val date3 = new AttributeValue
    date3.attribute = date1.attribute
    date3.value = "2142353"

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
    obj.attributeValues.add(date1)
    obj.labels = "coldplay,jethro tull"

    val obj2 = new Obj
    obj2.id = 4
    obj2.name = "bryan robson"
    obj2.objectType = new ObjectType
    obj2.objectType.id = 2
    obj2.status = "Disabled"
    obj2.attributeValues.add(av2)
    obj2.attributeValues.add(av5)
    obj2.attributeValues.add(date3)
    obj2.labels = "coldplay"

    val obj3 = new Obj
    obj3.id = 20
    obj3.name = "steve mclaren"
    obj3.objectType = new ObjectType
    obj3.objectType.id = 3
    obj3.status = "Live"
    obj3.attributeValues.add(av3)
    obj3.attributeValues.add(av6)
    obj3.attributeValues.add(date2)

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

    test("sorting by attribute value with numbers") {

        val search = new SavedSearch
        search.sortType = Sort.Attribute
        search.sortAttribute = date1.attribute

        val results = service.search(search).refs
        assert(results.size === 3)
        assert(results(0).id === 4)
        assert(results(1).id === 20)
        assert(results(2).id === 2)
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
        assert(4 === results(0).attributes.size)
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

    test("that elastic does not err on null av inputs") {

        val av = new AttributeValue
        av.attribute = new Attribute
        av.attribute.id = 9184

        val obj = new Obj
        obj.id = 199
        obj.name = "null-av-object"
        obj.objectType = new ObjectType
        obj.objectType.id = 1
        obj.status = "Live"
        obj.attributeValues.add(av)

        val before = service.count
        service.index(obj)
        Thread.sleep(1200)
        val after = service.count
        assert(before + 1 === after)
    }

    test("label search works on labels with a space") {

        val search = new SavedSearch
        search.labels = "jethro tull"
        val result = service.search(search)
        assert(1 === result.refs.size)
    }

    test("label search works on objects with multiple labels") {

        val search = new SavedSearch
        search.labels = "coldplay"
        val result = service.search(search)

        assert(2 === result.refs.size)
    }

    test("search max results of 0 changes to default") {
        val search = new SavedSearch
        search.maxResults = 0
        val max = service._maxResults(search)
        assert(service.DEFAULT_MAX_RESULTS === max)
    }

    test("search max results cannot exceed hard limit") {
        val search = new SavedSearch
        search.maxResults = service.MAX_RESULTS_HARD_LIMIT + 1
        val max = service._maxResults(search)
        assert(service.MAX_RESULTS_HARD_LIMIT === max)
    }

    test("acceptable search max results is used") {
        val search = new SavedSearch
        search.maxResults = 34
        val max = service._maxResults(search)
        assert(34 === max)
    }

    test("search escapes invalid characters in attribute values") {

        val av = new AttributeValue
        av.attribute = new Attribute
        av.attribute.id = 9184
        av.attribute.name = "color"
        av.attribute.attributeType = AttributeType.Text
        av.id = 69945
        av.value = "red!!"

        val obj = new Obj
        obj.id = 1529
        obj.name = "jeans"
        obj.objectType = new ObjectType
        obj.objectType.id = 2234
        obj.status = "live"
        obj.attributeValues.add(av)

        service.index(obj)
        Thread.sleep(1200)

        val q = new SavedSearch
        q.attributeValues.add(av)
        val results = service.search(q)
        assert(1 === results.refs.size)
        assert(1529 === results.refs(0).id)
    }
}
