package com.liferay.scalapress.feeds.gbase

import org.springframework.stereotype.Component
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.feeds.FeedDao
import com.liferay.scalapress.dao.ObjectDao
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.dao.settings.InstallationDao

/** @author Stephen Samuel */
@Component
class GBaseCron {

    @Autowired var context: ScalapressContext = _
    @Autowired var feedDao: FeedDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var installationDao: InstallationDao = _

    @Scheduled(cron = "0 0 6/12 * * *")
    def run() {
        feedDao.findAll().filter(_.isInstanceOf[GBaseFeed]).foreach(g => {
            GoogleBaseService.run(objectDao,
                feedDao,
                installationDao.get,
                g.asInstanceOf[GBaseFeed],
                context.assetStore)
        })
    }
}
