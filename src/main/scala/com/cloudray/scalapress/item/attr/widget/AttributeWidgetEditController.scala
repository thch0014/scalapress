package com.cloudray.scalapress.item.attr.widget

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.widgets.WidgetDao
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.widgets.controller.WidgetContainerMapPopulator
import com.cloudray.scalapress.util.AllAttributesPopulator
import com.cloudray.scalapress.item.TypeDao

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/widget/attribute/{id}"))
class AttributeWidgetEditController(widgetDao: WidgetDao,
                                    context: ScalapressContext,
                                    var objectTypeDao: TypeDao)
  extends WidgetContainerMapPopulator with AllAttributesPopulator {

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def edit(@ModelAttribute("widget") w: AttributeWidget, model: ModelMap) = "admin/widget/attribute.vm"

  @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
  def save(@ModelAttribute("widget") w: AttributeWidget, model: ModelMap) = {
    widgetDao.save(w)
    edit(w, model)
  }

  @ModelAttribute("widget")
  def widget(@PathVariable("id") id: Long) = widgetDao.find(id).asInstanceOf[AttributeWidget]

}