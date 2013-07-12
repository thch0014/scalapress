package com.cloudray.scalapress.plugin.variations

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
@Controller
@RequestMapping(value = Array("/backoffice/plugin/variations/dimensions/{id}"))
class DimensionEditController {

  @Autowired var dimensionDao: DimensionDao = _
  @Autowired var context: ScalapressContext = _

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit = "admin/plugin/variations/dimensions/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("dimension") dimension: Dimension) = {
    dimensionDao.save(dimension)
    "redirect:/backoffice/plugin/variations/dimensions"
  }

  @ModelAttribute("dimension") def dimension(@PathVariable("id") id: Long) = dimensionDao.find(id)
}
