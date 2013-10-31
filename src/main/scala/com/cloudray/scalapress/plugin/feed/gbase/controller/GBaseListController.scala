package com.cloudray.scalapress.plugin.feed.gbase.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.plugin.feed.gbase.{GBaseFeedDao, GBaseFeed}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/feed"))
class GBaseListController(feedDao: GBaseFeedDao,
                          context: ScalapressContext) {

  @RequestMapping
  def list = "admin/feed/list.vm"

  @RequestMapping(Array("create"))
  def create = {
    val gbase = new GBaseFeed
    feedDao.save(gbase)
    "redirect:/backoffice/feed"
  }

  @ModelAttribute("feeds")
  def feeds = feedDao.findAll.asJava
}
