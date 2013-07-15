package com.cloudray.scalapress.plugin.search.elastic

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.{ObjectType, Obj}
import com.cloudray.scalapress.media.Image
import com.cloudray.scalapress.enums.{AttributeType, Sort}
import com.cloudray.scalapress.obj.attr.{AttributeValue, Attribute}
import com.cloudray.scalapress.search.{SearchService, SavedSearch}
import scala.collection.JavaConverters._
import com.cloudray.scalapress.plugin.search.elasticsearch.ElasticSearchService
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
class ElasticSearchServiceTest extends FunSuite with MockitoSugar {

  val folder1 = new Folder
  folder1.name = "tickets"
  folder1.id = 4

  val folder2 = new Folder
  folder2.name = "events"
  folder2.id = 19

  val av1 = new AttributeValue
  av1.attribute = new Attribute
  av1.attribute.name = "team"
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
  av4.attribute.name = "postcode"
  av4.attribute.id = 2
  av4.attribute.attributeType = AttributeType.Postcode
  av4.value = "SW10"

  val av5 = new AttributeValue
  av5.attribute = av4.attribute
  av5.value = "SW10"

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
  date3.value = "999999999999999"

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
  obj2.folders.add(folder1)

  val obj3 = new Obj
  obj3.id = 20
  obj3.name = "steve mclaren"
  obj3.objectType = new ObjectType
  obj3.objectType.id = 3
  obj3.status = "Live"
  obj3.prioritized = true
  obj3.attributeValues.add(av3)
  obj3.attributeValues.add(av6)
  obj3.attributeValues.add(date2)
  obj3.folders.add(folder1)
  obj3.folders.add(folder2)

  val av = new AttributeValue
  av.attribute = new Attribute
  av.attribute.id = 9184
  av.attribute.name = "color"
  av.attribute.attributeType = AttributeType.Text
  av.id = 69945
  av.value = "red!!"

  val avWithSlash = new AttributeValue
  avWithSlash.attribute = new Attribute
  avWithSlash.attribute.id = 15922
  avWithSlash.attribute.name = "members"
  avWithSlash.attribute.attributeType = AttributeType.Text
  avWithSlash.id = 8125
  avWithSlash.value = "axel/slash"

  val obj4 = new Obj
  obj4.id = 1529
  obj4.name = "zola"
  obj4.objectType = new ObjectType
  obj4.objectType.id = 2234
  obj4.status = "live"
  obj4.attributeValues.add(av)
  obj4.attributeValues.add(avWithSlash)

  val service = new ElasticSearchService

  service
    .setupIndex(List(av1.attribute,
    av4.attribute,
    av7.attribute,
    date1.attribute))
  service.index(obj2)
  service.index(obj3)
  service.index(obj)
  service.index(obj4)

  Thread.sleep(1500)

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
    assert(results.size === 4)
    assert(results(0).id === 20)
    assert(results(1).id === 4)
    assert(results(2).id === 2)
    assert(results(3).id === 1529)
  }

  test("sorting by Newest happy path") {

    val search = new SavedSearch
    search.sortType = Sort.Newest
    val results = service.search(search).refs
    assert(results.size === 4)

    assert(results(0).id === 20)
    assert(results(1).id === 1529)
    assert(results(2).id === 4)
    assert(results(3).id === 2)
  }

  test("sorting by Oldest happy path") {

    val search = new SavedSearch
    search.sortType = Sort.Oldest
    val results = service.search(search).refs
    assert(results.size === 4)

    assert(results(0).id === 20)
    assert(results(1).id === 2)
    assert(results(2).id === 4)
    assert(results(3).id === 1529)
  }

  test("sorting by attribute value puts missing values at start") {

    val search = new SavedSearch
    search.sortType = Sort.Attribute
    search.sortAttribute = av1.attribute

    val results = service.search(search).refs
    assert(results.size === 4)

    assert(results(0).id === 20)
    assert(results(1).id === 1529)
    assert(results(2).id === 2)
    assert(results(3).id === 4)
  }

  test("sorting by attribute desc value happy path") {

    val search = new SavedSearch
    search.sortType = Sort.AttributeDesc
    search.sortAttribute = av1.attribute

    val results = service.search(search).refs
    assert(results.size === 4)

    assert(results(0).id === 20) // steve mac is prioritized
    assert(results(1).id === 4) // middles
    assert(results(2).id === 2) // mackams
  }

  test("sorting by attribute value with numbers") {

    val search = new SavedSearch
    search.sortType = Sort.Attribute
    search.sortAttribute = date1.attribute

    val results = service.search(search).refs
    assert(results.size === 4)

    assert(20 === results(0).id) // steve mac is prioritized
    assert(2 === results(1).id) // obj id 2 has value 51454
    assert(4 === results(2).id) // obj id 4 has value 2142353
    assert(1529 === results(3)
      .id) // obj id 1529 has no value for this attribute so will be last
  }

  test("distance search happy path") {

    val search = new SavedSearch
    search.location = "SW11"
    search
      .distance = 100 // only two should be within 100m, TS19 should be 250m away
    search.sortType = Sort.Distance
    assert(service.search(search).refs.size === 2)

    search
      .distance = 300 // only two should be within 100m, TS19 should be 250m away
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

  test("ref includes prioritized") {
    val search = new SavedSearch
    search.keywords = "steve"

    val results = service.search(search).refs
    assert(results.size === 1)
    assert(20 === results(0).id)
    assert(results(0).prioritized)

    val search2 = new SavedSearch
    search2.keywords = "mowbray"

    val results2 = service.search(search2).refs
    assert(results2.size === 1)
    assert(2 === results2(0).id)
    assert(!results2(0).prioritized)
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
    assert(4 === count)
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
    assert(1 === result
      .facets
      .head
      .terms
      .find(_.term == "jethro tull")
      .get
      .count)
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
    Thread.sleep(1500)

    val after = service.count
    assert(before + 1 === after)
  }

  test("that remove operation removes the item from the index") {

    val search = new SavedSearch
    assert(5 === service.search(search).count)

    val obj = new Obj
    obj.id = 5465464
    obj.name = "to be removed"
    obj.objectType = new ObjectType
    obj.objectType.id = 1
    obj.status = "Live"

    service.index(obj)
    Thread.sleep(1500)
    assert(6 === service.search(search).count)

    service.remove(obj.id.toString)
    Thread.sleep(1500)
    assert(5 === service.search(search).count)
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

    val q = new SavedSearch
    q.attributeValues.add(av)
    val results = service.search(q)
    assert(1 === results.refs.size)
    assert(1529 === results.refs(0).id)
  }

  test("contains returns true when the id exists") {
    assert(service.contains("4"))
  }

  test("contains returns false when the id does not exist") {
    assert(!service.contains("44"))
  }

  test("facets returned happy path") {
    val q = new SavedSearch
    q.facets = Seq(av4.attribute.id.toString)
    val results = service.search(q)
    assert(1 === results.facets.size)
    assert(2 === results.facets(0).terms.size)
    assert(2 === results.facets(0).terms(0).count)
    assert(1 === results.facets(0).terms(1).count)
    assert("SW10" === results.facets(0).terms(0).term)
    assert("TS19" === results.facets(0).terms(1).term)
    assert(2 === results.facets(0).terms.size)
    assert("2" === results.facets(0).field) // should be id of the attribute
  }

  test("facets should not include specified fields") {
    val av1 = new AttributeValue
    av1.attribute = av4.attribute
    av1.value = "SW10"

    val q = new SavedSearch
    q.attributeValues = Set(av1).asJava
    q.facets = Seq(av4.attribute.id.toString)
    val results = service.search(q)
    assert(0 === results.facets.size)
  }

  test("facets should be restored with spaces") {
    val av1 = new AttributeValue
    av1.attribute = av7.attribute
    av1.value = "attribute with space"

    val q = new SavedSearch
    q.facets = Seq(av7.attribute.id.toString)
    val results = service.search(q)
    assert("attribute with space" === results.facets(0).terms(0).term)
  }

  test("a service for folders should match objects with multiple folders") {
    val q = new SavedSearch
    q.searchFolders = "4"
    val results = service.search(q)
    assert(results.refs.map(_.id).contains(4))
  }

  test("a service for folders should match objects with single folders") {
    val q = new SavedSearch
    q.searchFolders = "4"
    val results = service.search(q)
    assert(results.refs.map(_.id).contains(20))
  }

  test("a random sort is sufficiently random") {
    val q = new SavedSearch
    q.sortType = Sort.Random
    // all first refs will be id 20 so check that 2nd refs are random
    val refs = for ( k <- 1 to 20 ) yield service.search(q).refs(1)
    assert(refs.toSet.size > 1)
  }

  test("searching for slash in attribute") {
    val av = new AttributeValue
    av.attribute = avWithSlash.attribute
    av.value = "axel/slash"

    val q = new SavedSearch
    q.attributeValues.add(av)
    val results = service.search(q)
    assert("zola" === results.refs(0).name)
  }

  test("searching for slash in free text") {
    val q = new SavedSearch
    q.name = "with / slash"
    val results = service.search(q)
    assert(results.refs.isEmpty)
  }

  test("ignore past should ignore documents prior to the date") {
    val q = new SavedSearch
    q.ignorePast = date1.attribute
    val results = service.search(q)
    assert(1 === results.refs.size)
  }
}
