package com.liferay.scalapress.plugin.search.widget

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import com.liferay.scalapress.widgets.Widget
import com.liferay.scalapress.controller.admin.widgets.WidgetEditController
import org.springframework.ui.ModelMap

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/search/widget/searchform/{id}"))
class SearchFormWidgetController extends WidgetEditController {

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    override def edit(@ModelAttribute("widget") w: Widget, model: ModelMap) = "admin/plugin/search/widget/searchform.vm"
}

