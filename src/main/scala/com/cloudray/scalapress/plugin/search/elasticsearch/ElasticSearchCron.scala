package com.cloudray.scalapress.plugin.search.elasticsearch

import com.cloudray.scalapress.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Component

/** @author Stephen Samuel */
@Component
@Autowired
class ElasticSearchCron(indexer: ElasticSearchIndexer) extends Runnable with Logging {

  var lastRuntime: Long = 0

  @Scheduled(fixedDelay = 1000 * 60 * 5, initialDelay = 0)
  @Transactional
  def run() {
    lastRuntime match {
      case 0 =>
        logger.info("Starting elastic search FULL index")
        indexer.fullIndex()
        lastRuntime = System.currentTimeMillis()
        logger.info("Full index has completed")
      case _ =>
        logger.info("Starting elastic search incremental index [lastRuntime={}]", lastRuntime)
        indexer.incrementalIndex(lastRuntime)
        logger.info("Incremental finished")
    }
  }
}