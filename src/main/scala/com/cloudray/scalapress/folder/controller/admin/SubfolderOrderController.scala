package com.cloudray.scalapress.folder.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import scala.Array
import com.cloudray.scalapress.folder.{FolderDao, Folder}
import org.springframework.ui.ModelMap
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/folder/{id}/suborder"))
class SubfolderOrderController {

    @Autowired var folderDao: FolderDao = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute folder: Folder) = "admin/folder/suborder.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def reorderSections(@RequestBody order: String, @ModelAttribute folder: Folder): String = {

        val ids = order.split("-")
        folder.subfolders.asScala.foreach(subfolder => {
            val pos = ids.indexOf(subfolder.id.toString)
            subfolder.position = pos
            folderDao.save(subfolder)
        })
        "ok"
    }

    @ModelAttribute def folder(@PathVariable("id") id: Long, map: ModelMap) {
        val folder = folderDao.find(id)
        map.put("folder", folder)
    }
}
