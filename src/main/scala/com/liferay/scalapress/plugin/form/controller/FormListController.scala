package com.liferay.scalapress.plugin.form.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/form"))
class FormListController {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def list = "admin/form/list.vm"

    import scala.collection.JavaConverters._

    @ModelAttribute("forms") def forms = context.formDao.findAll().asJava

}
