package com.cloudray.scalapress.search

import com.cloudray.scalapress.item._
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel
  *
  *         An implementation of SearchService that simply defers to the backing database.
  * */
class DatabaseSearchService extends SearchService {

  @Autowired
  var objectDao: ItemDao = _

  override def count: Long = objectDao.count(new com.googlecode.genericdao.search.Search(classOf[Item]))
  override def exists(id: String): Boolean = objectDao.find(id.toLong) != null

  override def search(search: Search): SearchResult = {

    val q = new ItemQuery().withStatus(Item.STATUS_LIVE)

    search.name.foreach(q.withName)
    search.itemTypeId.foreach(id => q.withTypeId(id.toString.toLong))

    q.withPageSize(search.maxResults)

    if (search.minPrice > 0) {
      q.withMinPrice(search.minPrice)
    }

    if (search.maxPrice > 0) {
      q.withMaxPrice(search.maxPrice)
    }

    q.withSort(search.sort)

    val objs = objectDao.search(q)
    val refs = objs.results.map(obj =>
      ItemRef(obj.id, obj.objectType.id, obj.name, obj.status, Map.empty, Nil, obj.prioritized))
    new SearchResult(refs, Nil, -1)
  }
}
