package com.cloudray.scalapress.plugin.search.elasticsearch

import com.googlecode.genericdao.search.Search
import com.cloudray.scalapress.item.Item
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import org.springframework.stereotype.Component
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import com.cloudray.scalapress.search.SearchService
import com.cloudray.scalapress.framework.{Logging, ScalapressContext}

/** @author Stephen Samuel */
trait ElasticSearchIndexer {
  def fullIndex(): Unit
  def incrementalIndex(since: Long): Unit
}

@Component
@Autowired
class ElasticSearchIndexerImpl(service: SearchService,
                               context: ScalapressContext) extends ElasticSearchIndexer with Logging {

  val PAGE_SIZE = 100

  @PostConstruct
  def setupIndexes() {
    val attributes = context.attributeDao.findAll
    service match {
      case service: ElasticSearchService => service.setupIndex(attributes)
      case _ =>
    }
  }

  def fullIndex(): Unit = index(_loadLive)

  def incrementalIndex(since: Long): Unit = index(_loadUpdated(_, _, since))

  def index(loader: (Int, Int) => Seq[Item]) {
    service match {
      case service: ElasticSearchService =>
        for ( objs <- iterator(loader) ) {
          for ( obj <- objs ) {
            try {
              service.index(obj)
            } catch {
              case e: Exception => logger.warn("{}", e)
            }
          }
        }
      case _ =>
    }
  }

  // returns an iterator of sequences of objs, that is, each element in the iterator is in fact
  // a sequence of objects that has been loaded in a single hit to the database.
  def iterator(loader: (Int, Int) => Seq[Item]): Iterator[Seq[Item]] = {
    new Iterator[Seq[Item]]() {
      var offset = 0
      var objs: Seq[Item] = Nil
      def next(): Seq[Item] = objs
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

  def _loadLive(offset: Int, pageSize: Int): Seq[Item] =
    context.itemDao.search(_basicSearch(offset, pageSize).addFilterLike("status", Item.STATUS_LIVE))

  def _loadUpdated(offset: Int, pageSize: Int, since: Long): Seq[Item] = {
    // include an extra overlap of 15 minutes to allow for time diffs on different nodes
    val _since = since - Duration(15, TimeUnit.MINUTES).toMillis
    context.itemDao.search(_basicSearch(offset, pageSize).addFilterGreaterOrEqual("dateUpdated", _since))
  }

  def _basicSearch(offset: Int, pageSize: Int) =
    new Search(classOf[Item]).setFirstResult(offset).setMaxResults(pageSize).addFilterNotNull("name")
}
