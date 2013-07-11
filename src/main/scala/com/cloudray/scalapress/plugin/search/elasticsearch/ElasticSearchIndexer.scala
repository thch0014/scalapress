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
  def index()
}

@Component
@ManagedResource(description = "Elasticsearch reindexer")
class ElasticSearchIndexerImpl extends ElasticSearchIndexer with Logging {

  @Autowired var service: ElasticSearchService = _
  @Autowired var context: ScalapressContext = _

  @PostConstruct
  def setupIndexes() {
    val attributes = context.attributeDao.findAll()
    service.setupIndex(attributes)
  }

  @Transactional
  @ManagedOperation(description = "perform a reindex")
  def index() {

    val pageSize = 100

    def _load(offset: Int) = {
      logger.debug("Loading {} results from offset {}", pageSize, offset)
      context.objectDao.search(new Search(classOf[Obj])
        .setFirstResult(offset)
        .setMaxResults(pageSize)
        .addFilterLike("status", "live")
        .addFilterNotEqual("objectType.name", "Account")
        .addFilterNotEqual("objectType.name", "account")
        .addFilterNotEqual("objectType.name", "Accounts")
        .addFilterNotEqual("objectType.name", "accounts"))
    }

    def _index(offset: Int) {
      logger.info("Indexing from offset [{}]", offset)
      val objs = _load(offset).filterNot(_.name == null).filterNot(_.name.isEmpty)
      if (!objs.isEmpty) {
        objs.foreach(obj => {
          try {
            service.index(obj)
          } catch {
            case e: Exception => logger.warn("{}", e)
          }
        })
        _index(offset + pageSize)
      }
    }

    _index(0)

    logger.info("Indexing finished")
  }
}
