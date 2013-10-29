package com.cloudray.scalapress.search.widget

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import com.cloudray.scalapress.widgets.{WidgetDao, Widget}
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.widgets.controller.WidgetEditController
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.item.controller.admin.MarkupPopulator
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/search/widget/results/{id}"))
@Autowired
class SearchResultsWidgetController(val markupDao: MarkupDao, widgetDao: WidgetDao)
  extends WidgetEditController(widgetDao: WidgetDao) with MarkupPopulator {

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  override def edit(@ModelAttribute("widget") w: Widget, model: ModelMap) = "admin/search/widget/results.vm"

}

