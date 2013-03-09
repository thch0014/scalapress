package com.liferay.scalapress.controller.admin.themes.markup

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.MarkupDao
import scala.Array

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/markup"))
class MarkupListController {

    @Autowired var markupDao: MarkupDao = _

    @RequestMapping(produces = Array("text/html"))
    def list = "admin/markup/list.vm"

    import scala.collection.JavaConverters._

    @ModelAttribute("markups") def markups = markupDao.findAll().asJava
}
