package com.cloudray.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.cloudray.scalapress.ScalapressContext
import scala.collection.JavaConverters._
import com.cloudray.scalapress.search.SavedSearchDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/savedsearch"))
class SavedSearchListController {

  @Autowired var dao: SavedSearchDao = _
  @Autowired var context: ScalapressContext = _

  @RequestMapping(produces = Array("text/html"))
  def list = "admin/savedsearch/list.vm"

  @ModelAttribute("searches") def searches = dao.findAll().asJava
}
