package com.cloudray.scalapress.plugin.form.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, RequestParam, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.cloudray.scalapress.ScalapressContext
import org.springframework.ui.ModelMap
import javax.servlet.http.HttpServletRequest
import com.sksamuel.scoot.soa.{PagedQuery, Paging}
import com.cloudray.scalapress.plugin.form.SubmissionDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/submission"))
class SubmissionListController {

    @Autowired var submissionDao: SubmissionDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def list = "admin/plugin/form/submissions/list.vm"

    @ModelAttribute def submissions(req: HttpServletRequest,
                                    @RequestParam(value = "pageNumber", defaultValue = "1") pageNumber: Int,
                                    model: ModelMap) {
        val subs = submissionDao.search(PagedQuery(pageNumber, 50))
        model.put("submissions", subs.java)
        model.put("paging", Paging(req, subs))
    }

    @RequestMapping(Array("{id}/delete"))
    def delete(@PathVariable("id") id: Long) = {
        submissionDao.removeById(id)
        "redirect:/backoffice/submission"
    }
}
