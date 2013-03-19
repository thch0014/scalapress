package com.liferay.scalapress.controller.admin.folder

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.folder.{FolderDao, Folder}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/folder"))
class FolderSearchController {

    @Autowired var folderDao: FolderDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def list = "admin/folder/list.vm"

    @RequestMapping(value = Array("create"))
    def create: String = {

        val root = folderDao.root
        val folder = Folder(root)
        folderDao.save(folder)

        "redirect:/backoffice/folder"
    }

    import scala.collection.JavaConverters._

    @ModelAttribute("folders") def folders = {
        folderDao.findAll().sortBy(_.fullName).asJava
    }
}
