package com.liferay.scalapress.obj.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, PathVariable, RequestMapping, ResponseBody}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import com.liferay.scalapress.media.{Image, ImageDao}
import com.liferay.scalapress.obj.{Obj, ObjectDao}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("rest/obj"))
class ObjectRestController {

    @Autowired var objectDao: ObjectDao = _
    @Autowired var imageDao: ImageDao = _

    @ResponseBody
    @RequestMapping(produces = Array(MediaType.APPLICATION_JSON_VALUE))
    def typeAhead(@RequestParam(value = "objectTypeName", required = false) objectTypeName: String,
                  @RequestParam("q") q: String): Array[Array[String]] =
        objectDao.typeAhead(q, Option(objectTypeName))

    @ResponseBody
    @RequestMapping(value = Array("{id}"), produces = Array(MediaType.APPLICATION_JSON_VALUE))
    def get(@PathVariable("id") id: Long): Obj = objectDao.find(id)

    @ResponseBody
    @RequestMapping(value = Array("{id}/image"), produces = Array(MediaType.APPLICATION_JSON_VALUE))
    def objects(@PathVariable("id") id: Long): Array[Image] = imageDao.findForObject(id)
}
