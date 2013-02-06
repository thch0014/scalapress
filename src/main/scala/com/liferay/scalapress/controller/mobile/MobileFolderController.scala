package com.liferay.scalapress.controller.mobile

import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.FolderDao
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMapping}
import org.springframework.ui.ModelMap
import org.springframework.stereotype.Controller
import com.liferay.scalapress.service.folder.FolderService

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("mobile/f"))
class MobileFolderController {

    @Autowired var folderDao: FolderDao = _
    @Autowired var folderService: FolderService = _

    @RequestMapping
    def home(model: ModelMap): String = {
        "mobile/general.vm"
    }

    @RequestMapping(value = Array("{id}"))
    def get(@PathVariable("id") id: Long, model: ModelMap): String = {
        val folder = folderDao.find(id)
        model.put("general", folder)
        model.put("objects", folderService.objects(id))
        "mobile/general.vm"
    }

    @ModelAttribute("folders") def folders = folderDao.findAll()
}

