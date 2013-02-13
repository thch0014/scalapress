package com.liferay.scalapress.feeds.gbase

import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.feeds.{Feed, FeedDao}
import com.liferay.scalapress.dao.ObjectDao
import com.liferay.scalapress.dao.settings.InstallationDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(value = Array("backoffice/feed/gbase/{id}"))
class GBaseController {

    @Autowired var context: ScalapressContext = _
    @Autowired var feedDao: FeedDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var installationDao: InstallationDao = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute feed: Feed) = "admin/feed/gbase/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute feed: Feed) = {
        feedDao.save(feed)
        edit(feed)
    }

    @RequestMapping(value = Array("run"))
    def run(@ModelAttribute feed: Feed) = {
        GoogleBaseService.run(objectDao, feedDao, installationDao.get, feed.asInstanceOf[GBaseFeed])
        "redirect:/backoffice/feed/"
    }

    @ModelAttribute def folder(@PathVariable("id") id: Long): Feed = feedDao.find(id)
}

