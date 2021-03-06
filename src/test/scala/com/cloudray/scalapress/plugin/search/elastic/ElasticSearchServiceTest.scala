package com.cloudray.scalapress.plugin.search.elastic

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.{ItemType, Item}
import com.cloudray.scalapress.item.attr._
import com.cloudray.scalapress.search.{Search, Sort}
import com.cloudray.scalapress.plugin.search.elasticsearch.ElasticSearchService
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.util.Page

/** @author Stephen Samuel */
class ElasticSearchServiceTest extends FunSuite with MockitoSugar {

  val folder1 = new Folder
  folder1.name = "folder1"
  folder1.id = 4

  val folder2 = new Folder
  folder2.name = "folder2"
  folder2.id = 19

  val av1 = new AttributeValue
  av1.id = 1
  av1.attribute = new Attribute
  av1.attribute.name = "team"
  av1.attribute.id = 1
  av1.value = "mackams"

  val av2 = new AttributeValue
  av2.id = 2
  av2.attribute = av1.attribute
  av2.value = "middlesbrough"

  val av3 = new AttributeValue
  av3.id = 3
  av3.attribute = av1.attribute
  av3.value = "barcodes"

  val av4 = new AttributeValue
  av4.id = 4
  av4.attribute = new Attribute
  av4.attribute.name = "postcode"
  av4.attribute.id = 2
  av4.attribute.attributeType = AttributeType.Postcode
  av4.value = "SW10"

  val av5 = new AttributeValue
  av5.id = 5
  av5.attribute = av4.attribute
  av5.value = "SW10"

  val av6 = new AttributeValue
  av6.id = 6
  av6.attribute = av4.attribute
  av6.value = "TS19"

  val av7 = new AttributeValue
  av7.id = 7
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

  val obj = new Item
  obj.id = 2
  obj.name = "tony mowbray captain bankrupt1986"
  obj.itemType = new ItemType
  obj.itemType.id = 1
  obj.status = Item.STATUS_LIVE
  obj.images.add("tony.png")
  obj.attributeValues.add(av1)
  obj.attributeValues.add(av4)
  obj.attributeValues.add(av7)
  obj.attributeValues.add(date1)
  obj.labels = "coldplay,jethro tull"
  obj.folders.add(folder2)

  val obj2 = new Item
  obj2.id = 4
  obj2.name = "bryan robson captain"
  obj2.itemType = new ItemType
  obj2.itemType.id = 2
  obj2.status = Item.STATUS_LIVE
  obj2.attributeValues.add(av2)
  obj2.attributeValues.add(av5)
  obj2.attributeValues.add(date3)
  obj2.labels = "coldplay"
  obj2.folders.add(folder1)

  val obj3 = new Item
  obj3.id = 20
  obj3.name = "steve mclaren"
  obj3.itemType = new ItemType
  obj3.itemType.id = 3
  obj3.status = Item.STATUS_LIVE
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

  val obj4 = new Item
  obj4.id = 1529
  obj4.name = "zola"
  obj4.itemType = new ItemType
  obj4.itemType.id = 2234
  obj4.status = Item.STATUS_LIVE
  obj4.attributeValues.add(av)
  obj4.attributeValues.add(avWithSlash)

  val attributeDao = mock[AttributeDao]
  val service = new ElasticSearchService(attributeDao)

  service.setupIndex(List(av1.attribute, av4.attribute, av7.attribute, date1.attribute))
  service.index(Seq(obj2, obj3, obj, obj4))

  Thread.sleep(1500)

  println(service.count)

  test("indexing and retrieval by name happy path") {

    val search = Search("tony")
    val results = service.search(search).refs
    assert(results.size === 1)
    assert(results(0).id === 2)
  }

  test("multi name happy path") {

    val search = Search("tony mowbray")
    val results = service.search(search).refs
    assert(results.size === 1)
    assert(results(0).id === 2)
  }

  test("indexing and retrieval by object type happy path") {

    val search = Search(itemTypeId = Option(obj2.itemType.id.toLong))
    val results = service.search(search).refs
    assert(results.size === 1)
    assert(results(0).id === 4)
  }

  test("indexing and retrieval by only images happy path") {

    val search = Search(imagesOnly = true)
    val results = service.search(search).refs
    assert(results.size === 1)
    assert(results(0).id === 2)
  }

  test("sorting by name happy path") {

    val search = Search(sort = Sort.Name)
    val results = service.search(search).refs
    assert(results.size === 4)
    assert(results(0).id === 20)
    assert(results(1).id === 4)
    assert(results(2).id === 2)
    assert(results(3).id === 1529)
  }

  test("sorting by Newest happy path") {

    val search = new Search(sort = Sort.Newest)
    val results = service.search(search).refs
    assert(results.size === 4)

    assert(results(0).id === 20)
    assert(results(1).id === 1529)
    assert(results(2).id === 4)
    assert(results(3).id === 2)
  }

  test("sorting by Oldest happy path") {

    val search = new Search(sort = Sort.Oldest)
    val results = service.search(search).refs
    assert(results.size === 4)

    assert(results(0).id === 20)
    assert(results(1).id === 2)
    assert(results(2).id === 4)
    assert(results(3).id === 1529)
  }

  test("sorting by attribute value puts missing values at end") {

    val search = new Search(sort = Sort.Attribute, sortAttribute = Option(av1.attribute))

    val results = service.search(search).refs
    assert(results.size === 4)

    assert(results(0).id === 20)
    assert(results(1).id === 2)
    assert(results(2).id === 4)
    assert(results(3).id === 1529)

  }

  test("sorting by attribute desc value happy path") {

    val search = new Search(sort = Sort.AttributeDesc, sortAttribute = Option(av1.attribute))

    val results = service.search(search).refs
    assert(results.size === 4)

    assert(results(0).id === 20) // steve mac is prioritized
    assert(results(1).id === 4) // middles
    assert(results(2).id === 2) // mackams
  }

  test("sorting by attribute value with numbers") {

    val search = new Search(sort = Sort.Attribute, sortAttribute = Option(date1.attribute))

    val results = service.search(search).refs
    assert(results.size === 4)

    assert(20 === results(0).id) // steve mac is prioritized
    assert(2 === results(1).id) // item id 2 has value 51454
    assert(4 === results(2).id) // item id 4 has value 2142353
    assert(1529 === results(3)
      .id) // item id 1529 has no value for this attribute so will be last
  }

  test("distance search happy path") {

    // only two should be within 100m, TS19 should be 250m away
    val search1 = Search(location = Option("SW11"), distance = 100, sort = Sort.Distance)
    assert(service.search(search1).refs.size === 2)

    val search2 = Search(location = Option("SW11"), distance = 300, sort = Sort.Distance)
    // only two should be within 100m, TS19 should be 250m away
    assert(service.search(search2).refs.size === 3)
  }

  test("resp2ref happy path") {

    val search = Search("mowbray")

    val results = service.search(search).refs
    assert(results.size === 1)
    assert(2 === results(0).id)
    assert("tony mowbray captain bankrupt1986" === results(0).name)
    assert("Live" === results(0).status)
    assert(4 === results(0).attributes.size)
  }

  test("ref includes prioritized") {
    val search = Search("steve")

    val results = service.search(search).refs
    assert(results.size === 1)
    assert(20 === results(0).id)
    assert(results(0).prioritized)

    val search2 = Search("mowbray")

    val results2 = service.search(search2).refs
    assert(results2.size === 1)
    assert(2 === results2(0).id)
    assert(!results2(0).prioritized)
  }

  test("attribute search with spaces") {

    val searchav = AttributeSelection(62, "attribute with space")
    val search = Search(attributeValues = List(searchav))
    val results = service.search(search).refs
    assert(results.size === 1)
    assert(2 === results(0).id)
    assert("tony mowbray captain bankrupt1986" === results(0).name)
  }

  test("wildcard search count brings back total count") {
    val search = Search()
    val count = service.search(search).count
    assert(4 === count) // includes dummy object
  }

  test("name search returns query based count") {
    val search = Search("mowbray")
    val count = service.search(search).count
    assert(1 === count)
  }

  test("attribute search returns query based count") {
    val searchav = AttributeSelection(62, "attribute with space")
    val search = Search(attributeValues = List(searchav))
    val count = service.search(search).count
    assert(1 === count)
  }

  //  test("wildcard search getting tag facets") {
  //
  //    val search = new SavedSearch
  //    search.facets = Seq(SearchService.FACET_TAGS)
  //    val result = service.search(search)
  //
  //    assert(1 === result.facets.size)
  //    assert(SearchService.FACET_TAGS === result.facets.head.name)
  //    assert(2 === result.facets.head.terms.find(_.term == "coldplay").get.count)
  //    assert(1 === result
  //      .facets
  //      .head
  //      .terms
  //      .find(_.term == "jethro tull")
  //      .get
  //      .count)
  //  }

  test("that elastic does not err on null av inputs") {

    val av = new AttributeValue
    av.attribute = new Attribute
    av.attribute.id = 9184

    val obj = new Item
    obj.id = 199
    obj.name = "null-av-object"
    obj.itemType = new ItemType
    obj.itemType.id = 1
    obj.status = "Live"
    obj.attributeValues.add(av)

    val before = service.count
    service.index(Seq(obj))
    Thread.sleep(1500)

    val after = service.count
    assert(before + 1 === after)
  }

  test("that remove operation removes the item from the index") {

    val search = new Search
    assert(5 === service.search(search).count)

    val obj = new Item
    obj.id = 5465464
    obj.name = "to be removed"
    obj.itemType = new ItemType
    obj.itemType.id = 1
    obj.status = "Live"

    service.index(Seq(obj))
    Thread.sleep(1500)
    assert(6 === service.search(search).count)

    service.remove(obj.id.toString)
    Thread.sleep(1500)
    assert(5 === service.search(search).count)
  }

  test("label search works on labels with a space") {

    val search = new Search(tags = List("jethro tull"))
    val result = service.search(search)
    assert(1 === result.refs.size)
  }

  test("label search works on objects with multiple labels") {

    val search = Search(tags = List("coldplay"))
    val result = service.search(search)

    assert(2 === result.refs.size)
  }

  test("search max results of 0 changes to default") {
    val search = Search(page = Page(1, 0))
    val max = service._maxResults(search)
    assert(service.DEFAULT_MAX_RESULTS === max)
  }

  test("search max results cannot exceed hard limit") {
    val search = Search(page = Page(1, service.MAX_RESULTS_HARD_LIMIT + 1))
    val max = service._maxResults(search)
    assert(service.MAX_RESULTS_HARD_LIMIT === max)
  }

  test("acceptable search pagesize is used") {
    val search = Search(page = Page(1, 7))
    val max = service._maxResults(search)
    assert(7 === max)
  }

  test("search escapes invalid characters in attribute values") {
    val q = new Search(attributeValues = List(AttributeSelection(9184, "red!!!!")))
    val results = service.search(q)
    assert(1 === results.refs.size)
    assert(1529 === results.refs(0).id)
  }

  test("contains returns true when the id exists") {
    assert(service.exists("4"))
  }

  test("contains returns false when the id does not exist") {
    assert(!service.exists("44"))
  }

  //  test("facets returned happy path") {
  //    val q = new SavedSearch
  //    q.facets = Seq(AttributeFacetField(av4.attribute.id))
  //    val results = service.search(q)
  //    assert(1 === results.facets.size)
  //    assert(2 === results.facets(0).terms.size)
  //    assert(2 === results.facets(0).terms(0).count)
  //    assert(1 === results.facets(0).terms(1).count)
  //    assert("SW10" === results.facets(0).terms(0).term)
  //    assert("TS19" === results.facets(0).terms(1).term)
  //    assert(2 === results.facets(0).terms.size)
  //    assert("2" === results.facets(0).field) // should be id of the attribute
  //  }

  //  test("facets should not include specified fields") {
  //    val av1 = new AttributeValue
  //    av1.attribute = av4.attribute
  //    av1.value = "SW10"
  //
  //    val q = new SavedSearch
  //    q.attributeValues = Set(av1).asJava
  //    q.facets = Seq(AttributeFacetField(av4.attribute.id))
  //    val results = service.search(q)
  //    assert(0 === results.facets.size)
  //  }

  //  test("facets should be restored with spaces") {
  //    val av1 = new AttributeValue
  //    av1.attribute = av7.attribute
  //    av1.value = "attribute with space"
  //
  //    val q = new SavedSearch
  //    q.facets = Seq(AttributeFacetField(av7.attribute.id))
  //    val results = service.search(q)
  //    assert("attribute with space" === results.facets(0).terms(0).term)
  //  }

  test("a service for folders should match objects with multiple folders") {
    val q = Search(folders = List("4"))
    val results = service.search(q)
    assert(2 === results.refs.size)
    assert(results.refs.map(_.id).contains(obj2.id))
    assert(results.refs.map(_.id).contains(obj3.id))
  }

  test("a service for folders should match objects with single folders") {
    val q = Search(folders = List("4"))
    val results = service.search(q)
    assert(results.refs.map(_.id).contains(20))
  }

  test("a random sort is sufficiently random") {
    val q = Search(sort = Sort.Random)
    // all first refs will be id 20 so check that 2nd refs are random
    val refs = for ( k <- 1 to 20 ) yield service.search(q).refs(1)
    assert(refs.toSet.size > 1)
  }

  test("searching for slash in attribute") {
    val q = Search(attributeValues = List(AttributeSelection(avWithSlash.attribute.id, "axel/slash")))
    val results = service.search(q)
    assert("zola" === results.refs(0).name)
  }

  test("searching for slash in free text") {
    val q = Search("with / slash")
    val results = service.search(q)
    assert(results.refs.isEmpty)
  }

  test("name search is a boolean AND") {
    val q = Search("captain tony")
    val results = service.search(q)
    assert(1 === results.refs.size)
  }

  test("ignore past should ignore documents prior to the date") {
    val q = Search(ignorePast = Option(date1.attribute))
    val results = service.search(q)
    assert(1 === results.refs.size)
  }

  test("a + character in a name query is stripped") {
    val q1 = Search("captain + tony")
    val results1 = service.search(q1)
    assert(1 === results1.refs.size)

    val q2 = Search("captain+tony")
    val results2 = service.search(q2)
    assert(1 === results2.refs.size)
  }

  test("ignore - character in search queries") {
    val q1 = Search("captain - tony")
    val results1 = service.search(q1)
    assert(1 === results1.refs.size)

    val q2 = Search("captain-tony")
    val results2 = service.search(q2)
    assert(1 === results2.refs.size)
  }


  test("ignore \" character in search queries") {
    val q1 = Search("captain \" tony")
    val results1 = service.search(q1)
    assert(1 === results1.refs.size)
  }

  //  test("strings are tokenized at letter/digit boundaries") {
  //    val q = new SavedSearch
  //    q.name = "1986"
  //    val results1 = service.search(q)
  //    assert(1 === results1.refs.size)
  //
  //    q.name = "bankrupt"
  //    val results2 = service.search(q)
  //    assert(2 === results2.refs.size)
  //  }
}
