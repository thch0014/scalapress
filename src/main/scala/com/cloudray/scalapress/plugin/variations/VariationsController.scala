package com.cloudray.scalapress.plugin.variations

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.obj.ObjectDao
import org.springframework.ui.ModelMap
import scala.collection.JavaConverters._

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
}
