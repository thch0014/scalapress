package com.cloudray.scalapress.folder.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.folder.{FolderDao, Folder}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/folder"))
class FolderSearchController(folderDao: FolderDao,
                             context: ScalapressContext) {

  @RequestMapping(produces = Array("text/html"))
  def list = "admin/folder/list.vm"

  @RequestMapping(value = Array("create"))
  def create: String = create("new folder")

  @RequestMapping(value = Array("create"), params = Array("name"))
  def create(@RequestParam("name") name: String): String = {

    val root = folderDao.root
    val folder = Folder(root)
    folder.name = name
    folderDao.save(folder)

    "redirect:/backoffice/folder/" + folder.id
  }

  @ModelAttribute("folders")
  def folders = folderDao.findAll.sortBy(_.fullName).asJava
}
