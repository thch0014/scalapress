package com.cloudray.scalapress.plugin.variations.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.obj.{Obj, ObjectDao}
import org.springframework.ui.ModelMap
import scala.collection.JavaConverters._
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.variations.{VariationCreationService, Dimension, VariationDao, DimensionDao}

/** @author Stephen Samuel */
@Controller
@RequestMapping(value = Array("/backoffice/plugin/variations"))
class VariationsController {

  @Autowired var dimensionDao: DimensionDao = _
  @Autowired var objectDao: ObjectDao = _
  @Autowired var variationDao: VariationDao = _

  @RequestMapping(method = Array(RequestMethod.GET), params = Array("objectId"))
  def edit(@RequestParam("objectId") id: Long, model: ModelMap) = {

    val obj = objectDao.find(id)
    model.put("obj", obj)

    val variations = variationDao.findByObjectId(id)
    model.put("variations", variations.asJava)

    val dimensions = dimensionDao.findAll().filterNot(_.objectType == null).filter(_.objectType.id == obj.objectType.id)
    model.put("dimensions", dimensions.asJava)

    "admin/plugin/variations/list.vm"
  }

  @RequestMapping(method = Array(RequestMethod.POST), params = Array("objectId"))
  def save(@RequestParam("objectId") id: Long, req: HttpServletRequest) = {
    val variations = variationDao.findByObjectId(id)
    for ( variation <- variations ) {
      variation.stock = Option(req.getParameter("stock_" + variation.id)).filterNot(_.isEmpty).getOrElse("0").toInt
      variation.price = Option(req.getParameter("price_" + variation.id)).filterNot(_.isEmpty).getOrElse("0").toInt
      variationDao.save(variation)
    }
    "redirect:/backoffice/plugin/variations?objectId=" + id
  }

  @RequestMapping(value = Array("{id}/delete"))
  def delete(@PathVariable("id") id: Long) = {
    val variation = variationDao.find(id)
    val objectId = variation.obj.id
    variationDao.remove(variation)
    "redirect:/backoffice/plugin/variations?objectId=" + objectId
  }

  def _dimensions(obj: Obj): Iterable[Dimension] = _dimensions(obj.objectType.id)
  def _dimensions(id: Long): Iterable[Dimension] = dimensionDao.findAll().filter(_.objectType.id == id)

  @RequestMapping(value = Array("add"), method = Array(RequestMethod.POST), params = Array("objectId"))
  def add(@RequestParam("objectId") id: Long, req: HttpServletRequest) = {

    val obj = objectDao.find(id)
    val values =
      for ( dimension <- _dimensions(obj) )
      yield (dimension, req.getParameter("dimension_" + dimension.id).split(",").map(_.trim).toList)
    val map = values.toMap
    val variations = new VariationCreationService(variationDao).create(obj, map)

    variations.foreach(v => variationDao.save(v))

    "redirect:/backoffice/plugin/variations?objectId=" + id
  }
}
