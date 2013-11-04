package com.cloudray.scalapress.plugin.search.elasticsearch

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Component
import com.cloudray.scalapress.settings.InstallationDao
import com.cloudray.scalapress.framework.Logging

/** @author Stephen Samuel */
@Component
@Autowired
class ElasticSearchCron(indexer: ElasticSearchIndexer,
                        installationDao: InstallationDao) extends Runnable with Logging {

  var lastRuntime: Long = 0

  @Scheduled(fixedDelay = 1000 * 60 * 5, initialDelay = 0)
  @Transactional
  def run() {
    lastRuntime match {
      case 0 =>
        logger.info("ElasticSearch full-index [site={}]", installationDao.get.domain)
        indexer.fullIndex()
        lastRuntime = System.currentTimeMillis()
        logger.info("Full index finished")
      case _ =>
        logger.info("ElasticSearch incremental-index [lastRuntime={}, site={}]",
          lastRuntime, installationDao.get.domain)
        indexer.incrementalIndex(lastRuntime)
        logger.info("Incremental index finished")
    }
  }
}