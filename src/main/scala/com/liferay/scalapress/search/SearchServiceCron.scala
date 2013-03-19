package com.liferay.scalapress.search

import org.springframework.stereotype.Component
import com.liferay.scalapress.{ScalapressContext, Logging}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.obj.ObjectDao

/** @author Stephen Samuel */
@Component
class SearchServiceCron extends CronTask with Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var searchService: SearchService = _

    @Scheduled(cron = "0 0/30 * * * *")
    @Transactional
    def run() {
        logger.info("Running search service index")
        searchService.index()
    }
}

trait CronTask {
    def run()
}