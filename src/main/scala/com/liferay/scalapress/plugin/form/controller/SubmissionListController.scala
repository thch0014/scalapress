package com.liferay.scalapress.plugin.form.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, RequestParam, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.liferay.scalapress.ScalapressContext
import org.springframework.ui.ModelMap
import javax.servlet.http.HttpServletRequest
import com.sksamuel.scoot.soa.{PagedQuery, Paging}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/submission"))
class SubmissionListController {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def list = "admin/submissions/list.vm"

    @ModelAttribute def submissions(req: HttpServletRequest,
                                    @RequestParam(value = "pageNumber", defaultValue = "1") pageNumber: Int,
                                    model: ModelMap) {
        val subs = context.submissionDao.search(PagedQuery(pageNumber, 50))
        model.put("submissions", subs.java)
        model.put("paging", Paging(req, subs))
    }

    @RequestMapping(Array("{id}/delete"))
    def delete(@PathVariable("id") id: Long) = {
        context.submissionDao.removeById(id)
        "redirect:/backoffice/submission"
    }
}
