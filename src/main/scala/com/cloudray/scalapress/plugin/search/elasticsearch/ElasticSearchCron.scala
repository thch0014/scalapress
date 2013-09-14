package com.cloudray.scalapress.plugin.search.elasticsearch

import org.springframework.stereotype.Component
import com.cloudray.scalapress.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */
@Component
class ElasticSearchCron extends Runnable with Logging {

  @Autowired var indexer: ElasticSearchIndexer = _

  @Scheduled(fixedDelay = 1000 * 60 * 30, initialDelay = 0)
  @Transactional
  def run() {
    logger.info("Running search service index")
    indexer.index()
  }
}