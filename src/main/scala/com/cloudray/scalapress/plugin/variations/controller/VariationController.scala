package com.cloudray.scalapress.plugin.variations.controller

import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, ResponseBody}
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import com.cloudray.scalapress.plugin.variations.{DimensionDao, VariationDao, Variation}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.item.ItemDao
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("rest/plugin/variations"))
class VariationController {

  @Autowired var dimensionDao: DimensionDao = _
  @Autowired var variationDao: VariationDao = _
  @Autowired var objectDao: ItemDao = _

  @ResponseBody
  @RequestMapping(value = Array("{id}"), produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def variation(@PathVariable("id") id: Long, req: HttpServletRequest, resp: HttpServletResponse): Variation = {
    variationDao
      .findByObjectId(id)
      .find(v => v.dimensionValues.asScala.forall(dv => dv.value == req.getParameter(s"dimension_${dv.id}"))) match {
      case None =>
        resp.sendError(404)
        null
      case Some(variation) => variation
    }
  }
}
