package com.liferay.scalapress.feeds.gbase

import org.springframework.stereotype.Component
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.feeds.FeedDao
import com.liferay.scalapress.dao.ObjectDao

/** @author Stephen Samuel */
@Component
class GBaseCron {

    @Autowired var feedDao: FeedDao = _
    @Autowired var objectDao: ObjectDao = _

    @Scheduled(cron = "0 0 0/4 * * *")
    def run() {
        feedDao.findAll().filter(_.isInstanceOf[GBaseFeed]).foreach(g => {

        })
    }
}
