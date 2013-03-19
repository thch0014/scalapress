package com.liferay.scalapress.controller.rest

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, PathVariable, RequestMapping, ResponseBody}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{ImageDao, ObjectDao}
import org.springframework.http.MediaType
import com.liferay.scalapress.domain.{Image}
import com.liferay.scalapress.obj.Obj

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("rest/obj"))
class ObjectRestController {

    @Autowired var objectDao: ObjectDao = _
    @Autowired var imageDao: ImageDao = _

    @ResponseBody
    @RequestMapping
    def typeAhead(@RequestParam("q") q: String,
                  @RequestParam(value = "objectTypeName",
                      required = false) objectTypeName: String): Array[Array[String]] = objectDao
      .typeAhead(q, Option(objectTypeName))

    @ResponseBody
    @RequestMapping(value = Array("{id}"), produces = Array(MediaType.APPLICATION_JSON_VALUE))
    def get(@PathVariable("id") id: Long): Obj = objectDao.find(id)

    @ResponseBody
    @RequestMapping(value = Array("{id}/image"), produces = Array(MediaType.APPLICATION_JSON_VALUE))
    def objects(@PathVariable("id") id: Long): Array[Image] = imageDao.findForObject(id)
}
