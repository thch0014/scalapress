package com.liferay.scalapress.feeds.gbase

import org.springframework.stereotype.Component
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.feeds.FeedDao
import com.liferay.scalapress.{Logging, ScalapressContext}
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.obj.ObjectDao
import com.liferay.scalapress.settings.InstallationDao

/** @author Stephen Samuel */
@Component
class GBaseCron extends CronTask with Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var feedDao: FeedDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var installationDao: InstallationDao = _

    @Scheduled(cron = "0 0 12,18 * * *")
    @Transactional
    def run() {
        logger.info("Running GBase Cron")
        feedDao.findAll().filter(_.isInstanceOf[GBaseFeed]).foreach(g => {
            GoogleBaseService.run(objectDao,
                feedDao,
                installationDao.get,
                g.asInstanceOf[GBaseFeed],
                context.assetStore)
        })
    }
}

trait CronTask {
    def run()
}