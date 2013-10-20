package com.cloudray.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import scala.Array
import com.cloudray.scalapress.ScalapressContext
import scala.collection.JavaConverters._
import com.cloudray.scalapress.search.SavedSearchDao
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/savedsearch"))
@Autowired
class SavedSearchListController(dao: SavedSearchDao,
                                context: ScalapressContext) {

  @RequestMapping(produces = Array("text/html"))
  def list = "admin/savedsearch/list.vm"

  @ModelAttribute("searches") def searches = dao.findAll().asJava
}
