package com.liferay.scalapress.search.widget

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import com.liferay.scalapress.widgets.Widget
import org.springframework.ui.ModelMap
import com.liferay.scalapress.widgets.controller.WidgetEditController
import com.liferay.scalapress.controller.admin.obj.MarkupPopulator
import com.liferay.scalapress.dao.MarkupDao
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/search/widget/results/{id}"))
class SearchResultsWidgetController extends WidgetEditController with MarkupPopulator {

    @Autowired var markupDao: MarkupDao = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    override def edit(@ModelAttribute("widget") w: Widget, model: ModelMap) = "admin/search/widget/results.vm"


}

