package com.cloudray.scalapress.plugin.feed.gbase

import org.springframework.stereotype.Component
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.item.ItemDao
import com.cloudray.scalapress.settings.InstallationDao
import com.cloudray.scalapress.framework.{Logging, ScalapressContext}

/** @author Stephen Samuel */
@Component
class GBaseCron extends CronTask with Logging {

  @Autowired
  var context: ScalapressContext = _

  @Autowired
  var feedDao: GBaseFeedDao = _

  @Autowired
  var objectDao: ItemDao = _

  @Autowired
  var installationDao: InstallationDao = _

  @Scheduled(cron = "0 0 8,10,12,14,16,18 * * *")
  @Transactional
  def run() {
    logger.info("Running Google base cron...")
    feedDao.findAll.foreach(g => {
      logger.info("Processing feed [{}]", g)
      GoogleBaseService.run(objectDao, feedDao, installationDao.get, g, context.assetStore)
    })
  }
}

trait CronTask {
  def run()
}