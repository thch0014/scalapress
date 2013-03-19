package com.liferay.scalapress.controller.mobile

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMapping}
import org.springframework.ui.ModelMap
import org.springframework.stereotype.Controller
import scala.collection.JavaConverters._
import com.liferay.scalapress.folder.FolderDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("mobile"))
class MobileFolderController {

    @Autowired var folderDao: FolderDao = _

    @RequestMapping
    def home(model: ModelMap): String = {
        val folder = folderDao.root
        model.put("folder", folder)
        model.put("sections", folder.sections.asScala.toArray.sortBy(_.position))
        "mobile/folder.vm"
    }

    @RequestMapping(value = Array("{id}"))
    def get(@PathVariable("id") id: Long, model: ModelMap): String = {
        val folder = folderDao.find(id)
        model.put("folder", folder)
        model.put("sections", folder.sections.asScala.toArray.sortBy(_.position))
        "mobile/folder.vm"
    }

    @ModelAttribute("folders") def folders = folderDao.findAll().toArray
}

