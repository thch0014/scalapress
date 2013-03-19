package com.liferay.scalapress.theme

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/markup"))
class MarkupListController {

    @Autowired var markupDao: MarkupDao = _

    @RequestMapping(produces = Array("text/html"))
    def list = "admin/theme/markup/list.vm"

    @RequestMapping(value = Array("create"), produces = Array("text/html"))
    def create = {
        val markup = new Markup
        markup.name = "new markup"
        markupDao.save(markup)
        "redirect:/backoffice/markup/" + markup.id
    }

    import scala.collection.JavaConverters._

    @ModelAttribute("markups") def markups = markupDao.findAll().asJava
}
