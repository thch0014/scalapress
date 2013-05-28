package com.cloudray.scalapress.search.widget

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import com.cloudray.scalapress.widgets.Widget
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.widgets.controller.WidgetEditController

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/search/widget/searchform/{id}"))
class SearchFormWidgetController extends WidgetEditController {

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    override def edit(@ModelAttribute("widget") w: Widget, model: ModelMap) = "admin/plugin/search/widget/searchform.vm"
}

