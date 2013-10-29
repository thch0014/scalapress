package com.cloudray.scalapress.obj.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, PathVariable, RequestMapping, ResponseBody}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import com.cloudray.scalapress.media.ImageDao
import com.cloudray.scalapress.obj.{Item, ObjectDao}
import com.cloudray.scalapress.account.controller.Datum

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("rest/obj"))
class ObjectRestController(objectDao: ObjectDao,
                           imageDao: ImageDao) {

  @ResponseBody
  @RequestMapping(value = Array("{id}"), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def get(@PathVariable("id") id: Long): Item = objectDao.find(id)

  @ResponseBody
  @RequestMapping(value = Array("{id}/image"), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def objects(@PathVariable("id") id: Long): Array[String] = objectDao.find(id).sortedImages.toArray

  @ResponseBody
  @RequestMapping(produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def typeAhead(@RequestParam("q") q: String): Array[Datum] = objectDao.typeAhead(q)
}
