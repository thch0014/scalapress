package com.cloudray.scalapress.search.widget

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import com.cloudray.scalapress.widgets.Widget
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.widgets.controller.WidgetEditController
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.obj.controller.admin.MarkupPopulator

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/search/widget/results/{id}"))
class SearchResultsWidgetController extends WidgetEditController with MarkupPopulator {

    @Autowired var markupDao: MarkupDao = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    override def edit(@ModelAttribute("widget") w: Widget, model: ModelMap) = "admin/search/widget/results.vm"


}

