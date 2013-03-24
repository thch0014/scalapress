package com.liferay.scalapress.plugin.feed.gbase.controller

import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.obj.ObjectDao
import com.liferay.scalapress.settings.InstallationDao
import com.liferay.scalapress.plugin.feed.gbase.{GBaseFeedDao, GBaseFeed, GoogleBaseService}

/** @author Stephen Samuel */
@Controller
@RequestMapping(value = Array("backoffice/feed/gbase/{id}"))
class GBaseController {

    @Autowired var context: ScalapressContext = _
    @Autowired var feedDao: GBaseFeedDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var installationDao: InstallationDao = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute("feed") feed: GBaseFeed) = "admin/feed/gbase/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute("feed") feed: GBaseFeed) = {
        feedDao.save(feed)
        edit(feed)
    }

    @RequestMapping(value = Array("run"))
    def run(@ModelAttribute("feed") feed: GBaseFeed) = {
        GoogleBaseService.run(objectDao, feedDao, installationDao.get, feed, context.assetStore)
        "redirect:/backoffice/feed/"
    }

    @ModelAttribute("feed") def feed(@PathVariable("id") id: Long): GBaseFeed = feedDao.find(id)
}

