package com.cloudray.scalapress.plugin.variations

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, RequestParam, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import scala.collection.JavaConverters._
import com.cloudray.scalapress.obj.TypeDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(value = Array("/backoffice/plugin/variations/dimensions"))
class DimensionListController {

  @Autowired var dimensionDao: DimensionDao = _
  @Autowired var objectTypeDao: TypeDao = _
  @Autowired var context: ScalapressContext = _
  @Autowired var dimensionValueDao: DimensionValueDao = _

  @RequestMapping
  def list = "admin/plugin/variations/dimensions/list.vm"

  @RequestMapping(Array("create"))
  def create(@RequestParam("objectTypeId") objectTypeId: Long) = {

    val `type` = objectTypeDao.find(objectTypeId)

    val dimension = new Dimension
    dimension.name = "new"
    dimension.objectType = `type`

    dimensionDao.save(dimension)
    "redirect:/backoffice/plugin/variations/dimensions?objectTypeId=" + objectTypeId
  }

  @RequestMapping(Array("{id}/delete"))
  def delete(@PathVariable("id") id: Long) = {
    val dimension = dimensionDao.find(id)
    val objectTypeId = dimension.objectType.id
    //    val values = dimensionValueDao.findByDimension(dimension.id)
    //    values.foreach(dimensionValueDao remove)
    dimensionDao.remove(dimension)
    "redirect:/backoffice/plugin/variations/dimensions?objectTypeId=" + objectTypeId
  }

  @ModelAttribute("objectTypeId") def objectTypeId(@RequestParam("objectTypeId") objectTypeId: Long) = objectTypeId

  @ModelAttribute("dimensions") def dimensions(@RequestParam("objectTypeId") objectTypeId: Long) =
    dimensionDao.findAll().filterNot(_.objectType == null).filter(_.objectType.id == objectTypeId).asJava
}
