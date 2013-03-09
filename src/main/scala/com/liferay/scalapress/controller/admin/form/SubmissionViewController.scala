package com.liferay.scalapress.controller.admin.form

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/submission/{id}"))
class SubmissionViewController {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def list = "admin/submissions/view.vm"

    @ModelAttribute("assetStore") def assetStore = context.assetStore
    @ModelAttribute("submission") def submission(@PathVariable("id") id: Long) = context.submissionDao.find(id)
}
