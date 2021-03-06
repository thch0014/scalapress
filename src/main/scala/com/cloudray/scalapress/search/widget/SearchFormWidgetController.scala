package com.cloudray.scalapress.search.widget

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import com.cloudray.scalapress.widgets.{WidgetDao, Widget}
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.widgets.controller.WidgetEditController
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/search/widget/searchform/{id}"))
class SearchFormWidgetController(widgetDao: WidgetDao) extends WidgetEditController(widgetDao) {

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  override def edit(@ModelAttribute("widget") w: Widget, model: ModelMap) = "admin/plugin/search/widget/searchform.vm"
}

