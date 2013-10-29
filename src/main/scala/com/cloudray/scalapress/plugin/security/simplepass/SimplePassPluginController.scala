package com.cloudray.scalapress.plugin.security.simplepass

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import scala.collection.mutable
import com.cloudray.scalapress.folder.FolderDao
import scala.collection.immutable.ListMap
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/security/simplepass"))
class SimplePassPluginController(simplePassPluginDao: SimplePassPluginDao,
                                 folderDao: FolderDao) {

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("plugin") plugin: SimplePassPlugin) = "admin/plugin/security/simplepass/plugin.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("plugin") plugin: SimplePassPlugin,
           @RequestParam("folder") id: Long): String = {
    Option(folderDao.find(id)).foreach(plugin.folders.add)
    simplePassPluginDao.save(plugin)
    "redirect:/backoffice/plugin/security/simplepass"
  }

  @RequestMapping(value = Array("delete"))
  def deleteFolder(@ModelAttribute("plugin") plugin: SimplePassPlugin,
                   @RequestParam("folder") id: Long): String = {
    plugin.folders = plugin.folders.asScala.filterNot(_.id == id).asJava
    simplePassPluginDao.save(plugin)
    "redirect:/backoffice/plugin/security/simplepass"
  }

  @ModelAttribute("folders")
  def parents = {
    val folders = folderDao.findAll.sortBy(_.id)

    val map = mutable.Map(0l -> "-Default-")
    folders.map(f => {
      map += (f.id -> f.fullName)
    })

    val ordered = ListMap(map.toList.sortBy {
      _._2
    }: _*)

    ordered.asJava
  }

  @ModelAttribute("plugin")
  def plugin = simplePassPluginDao.get
}
