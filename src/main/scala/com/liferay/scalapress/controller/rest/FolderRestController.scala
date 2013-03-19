package com.liferay.scalapress.controller.rest

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, ResponseBody}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.folder.FolderDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("rest/general"))
class FolderRestController {

    @Autowired var folderDao: FolderDao = _

    @ResponseBody
    @RequestMapping(produces = Array(MediaType.APPLICATION_JSON_VALUE))
    def list = folderDao.findAll()

    @ResponseBody
    @RequestMapping(value = Array("{id}"), produces = Array(MediaType.APPLICATION_JSON_VALUE))
    def get(@PathVariable("id") id: Long) = folderDao.find(id)

    @ResponseBody
    @RequestMapping(value = Array("{id}/obj"), produces = Array(MediaType.APPLICATION_JSON_VALUE))
    def objects(@PathVariable("id") id: Long): Array[Obj] = Array()

}
