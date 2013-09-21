package com.cloudray.scalapress.plugin.search.elasticsearch

import org.springframework.transaction.annotation.Transactional
import com.googlecode.genericdao.search.Search
import com.cloudray.scalapress.obj.Obj
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import com.cloudray.scalapress.{Logging, ScalapressContext}
import org.springframework.stereotype.Component
import org.springframework.jmx.export.annotation.{ManagedOperation, ManagedResource}

/** @author Stephen Samuel */
trait ElasticSearchIndexer {
  def fullIndex(): Unit
  def incrementalIndex(since: Long): Unit
}

@Component
@ManagedResource(description = "Elasticsearch Indexer")
class ElasticSearchIndexerImpl extends ElasticSearchIndexer with Logging {

  val PAGE_SIZE = 100

  @Autowired var service: ElasticSearchService = _
  @Autowired var context: ScalapressContext = _

  @PostConstruct
  def setupIndexes() {
    val attributes = context.attributeDao.findAll()
    service.setupIndex(attributes)
  }

  @Transactional
  @ManagedOperation(description = "perform a full index")
  def fullIndex(): Unit = index(_loadAll)

  @Transactional
  @ManagedOperation(description = "perform an incremental index")
  def incrementalIndex(since: Long): Unit = {
    index(_loadUpdated(_, _, since))
  }

  def index(loader: (Int, Int) => Seq[Obj]) {
    logger.info("Starting index")
    for ( objs <- iterator(loader) ) {
      try {
        service.index(objs)
      } catch {
        case e: Exception => logger.warn("{}", e)
      }
    }
    logger.info("Indexing finished")
  }

  // returns an iterator of sequences of objs, that is, each element in the iterator is in fact
  // a sequence of objects that has been loaded in a single hit to the database.
  def iterator(loader: (Int, Int) => Seq[Obj]): Iterator[Seq[Obj]] = {
    new Iterator[Seq[Obj]]() {
      var offset = 0
      var objs: Seq[Obj] = Nil
      def next(): Seq[Obj] = objs
      def hasNext: Boolean = {
        _load()
        !objs.isEmpty
      }
      def _load(): Unit = {
        logger.debug("Loading objects [offset={}]", offset)
        objs = loader(offset, PAGE_SIZE)
        offset = offset + PAGE_SIZE
      }
    }
  }

  def _loadAll(offset: Int, pageSize: Int): Seq[Obj] = context.objectDao.search(_basicSearch(offset, pageSize))

  def _loadUpdated(offset: Int, pageSize: Int, since: Long): Seq[Obj] =
    context.objectDao.search(_basicSearch(offset, pageSize).addFilterGreaterThan("dateUpdated", since))

  def _basicSearch(offset: Int, pageSize: Int) = new Search(classOf[Obj])
    .setFirstResult(offset)
    .setMaxResults(pageSize)
    .addFilterLike("status", "live")
    .addFilterNotNull("name")
    .addFilterNotEqual("objectType.name", "Account")
    .addFilterNotEqual("objectType.name", "account")
    .addFilterNotEqual("objectType.name", "Accounts")
    .addFilterNotEqual("objectType.name", "accounts")
}
