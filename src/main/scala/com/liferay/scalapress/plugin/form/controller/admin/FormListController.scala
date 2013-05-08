package com.liferay.scalapress.plugin.form.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.form.Form

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/form"))
class FormListController {

    @Autowired var context: ScalapressContext = _

    @RequestMapping
    def list = "admin/plugin/form/list.vm"

    @RequestMapping(value = Array("create"))
    def create: String = {

        val form = new Form
        form.name = "new form"
        context.formDao.save(form)

        "redirect:/backoffice/form/" + form.id
    }

    import scala.collection.JavaConverters._

    @ModelAttribute("forms") def forms = context.formDao.findAll().asJava

}
