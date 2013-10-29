package com.cloudray.scalapress.item.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, PathVariable, RequestMapping, ResponseBody}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import com.cloudray.scalapress.media.ImageDao
import com.cloudray.scalapress.item.{Item, ItemDao}
import com.cloudray.scalapress.account.controller.Datum

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("rest/obj", "rest/item"))
class ItemRestController(itemDao: ItemDao,
                         imageDao: ImageDao) {

  @ResponseBody
  @RequestMapping(value = Array("{id}"), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def get(@PathVariable("id") id: Long): Item = itemDao.find(id)

  @ResponseBody
  @RequestMapping(value = Array("{id}/image"), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def objects(@PathVariable("id") id: Long): Array[String] = itemDao.find(id).sortedImages.toArray

  @ResponseBody
  @RequestMapping(produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def typeAhead(@RequestParam("q") q: String): Array[Datum] = itemDao.typeAhead(q)
}
