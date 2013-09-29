package com.cloudray.scalapress.plugin.search.elasticsearch

import com.googlecode.genericdao.search.Search
import com.cloudray.scalapress.obj.Obj
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import com.cloudray.scalapress.{Logging, ScalapressContext}
import org.springframework.stereotype.Component
import org.springframework.jmx.export.annotation.{ManagedOperation, ManagedResource}
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

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

  @ManagedOperation(description = "perform a full index")
  def fullIndex(): Unit = index(_loadLive)

  @ManagedOperation(description = "perform an incremental index")
  def incrementalIndex(since: Long): Unit = index(_loadUpdated(_, _, since))

  def index(loader: (Int, Int) => Seq[Obj]) {
    for ( objs <- iterator(loader) ) {
      for ( obj <- objs ) {
        try {
          service.index(obj)
        } catch {
          case e: Exception => logger.warn("{}", e)
        }
      }
    }
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

  def _loadLive(offset: Int, pageSize: Int): Seq[Obj] =
    context.objectDao.search(_basicSearch(offset, pageSize).addFilterLike("status", Obj.STATUS_LIVE))

  def _loadUpdated(offset: Int, pageSize: Int, since: Long): Seq[Obj] = {
    // include an extra overlap of 15 minutes to allow for time diffs on different nodes
    val _since = since - Duration(15, TimeUnit.MINUTES).toMillis
    context.objectDao.search(_basicSearch(offset, pageSize).addFilterGreaterOrEqual("dateUpdated", _since))
  }

  def _basicSearch(offset: Int, pageSize: Int) = new Search(classOf[Obj])
    .setFirstResult(offset)
    .setMaxResults(pageSize)
    .addFilterNotNull("name")
    .addFilterNotEqual("objectType.name", "Account")
    .addFilterNotEqual("objectType.name", "account")
    .addFilterNotEqual("objectType.name", "Accounts")
    .addFilterNotEqual("objectType.name", "accounts")
}
