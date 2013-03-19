package com.liferay.scalapress.obj

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.ModelMap
import com.liferay.scalapress.folder.FolderDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("mobile/o"))
class MobileObjectController {

    @Autowired var folderDao: FolderDao = _
    @Autowired var objectDao: ObjectDao = _

    @RequestMapping(value = Array("{cat}/{id}/{name}"))
    def getWithCatAndName(@PathVariable("id") id: Long, model: ModelMap): String = get(id, model)

    @RequestMapping(value = Array("{id}/{name}"))
    def getWithName(@PathVariable("id") id: Long, model: ModelMap): String = get(id, model)

    @RequestMapping(value = Array("{id}"))
    def get(@PathVariable("id") id: Long, model: ModelMap): String = {
        val obj = objectDao.find(id)
        model.put("obj", obj)
        "mobile/obj.vm"
    }

    @ModelAttribute("folders") def folders = folderDao.findAll().toArray
}
