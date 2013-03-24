package com.liferay.scalapress.plugin.feed.gbase.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.feed.gbase.{GBaseFeedDao, GBaseFeed}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/feed"))
class GBaseListController {

    @Autowired var feedDao: GBaseFeedDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping
    def list = "admin/feed/list.vm"

    @RequestMapping(Array("create"))
    def create = {
        val gbase = new GBaseFeed
        feedDao.save(gbase)
        "redirect:/backoffice/feed"
    }

    @ModelAttribute("feeds") def feeds = feedDao.findAll().asJava
}
