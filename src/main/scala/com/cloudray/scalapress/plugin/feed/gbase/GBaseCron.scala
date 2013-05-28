package com.cloudray.scalapress.plugin.feed.gbase

import org.springframework.stereotype.Component
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.{Logging, ScalapressContext}
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.obj.ObjectDao
import com.cloudray.scalapress.settings.InstallationDao

/** @author Stephen Samuel */
@Component
class GBaseCron extends CronTask with Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var feedDao: GBaseFeedDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var installationDao: InstallationDao = _

    @Scheduled(cron = "0 0 12,18 * * *")
    @Transactional
    def run() {
        logger.info("Running GBase Cron")
        feedDao.findAll().foreach(g => {
            GoogleBaseService.run(objectDao,
                feedDao,
                installationDao.get,
                g,
                context.assetStore)
        })
    }
}

trait CronTask {
    def run()
}