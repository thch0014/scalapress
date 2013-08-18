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

    var offset = 0
    val pageSize = 100
    var objs = _load(offset, pageSize).filterNot(_.name == null).filterNot(_.name.isEmpty)
    while (!objs.isEmpty) {
      logger.info("Indexing from offset [{}]", offset)
      objs.foreach(obj => {
        try {
          service.index(obj)
        } catch {
          case e: Exception => logger.warn("{}", e)
        }
      })
      offset = offset + pageSize
      objs = _load(offset, pageSize).filterNot(_.name == null).filterNot(_.name.isEmpty)
    }
    logger.info("Indexing finished")
  }

  def _load(offset: Int, pageSize: Int): Seq[Obj] = {
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
}
