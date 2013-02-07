package com.liferay.scalapress.plugin.form

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, PathVariable, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.dao.{ThemeDao, FormDao, SubmissionDao}
import com.liferay.scalapress.{Logging, ScalapressRequest}
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.controller.web.folder.FolderController
import org.springframework.web.multipart.MultipartFile
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("form/{id}"))
class SubmissionController extends Logging {

    @Autowired var submissionDao: SubmissionDao = _
    @Autowired var formDao: FormDao = _
    @Autowired var formService: FormService = _
    @Autowired var themeDao: ThemeDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var folderController: FolderController = _

    @ResponseBody
    @RequestMapping(produces = Array("text/html"), value = Array("test"))
    def view(@PathVariable("id") id: Long,
             req: HttpServletRequest) = {

        val form = formDao.find(id)
        val sub = formService.doSubmission(form, req, Nil)
        formService.email(List("sam@sksamuel.com", "info@clocked0ne.co.uk"), sub)

        "email sent"
    }

    @ResponseBody
    @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
    def view(@PathVariable("id") id: Long,
             req: HttpServletRequest,
             resp: HttpServletResponse,
             @RequestParam(value = "file") files: java.util.List[MultipartFile],
             @RequestParam(value = "folder", required = false) folderId: Long): ScalaPressPage = {

        val sreq = ScalapressRequest(req)
        val form = formDao.find(id)

        formService.checkErrors(form, sreq)

        sreq.hasErrors match {
            case true => {
                logger.debug("Form has errors {}, redirecting to folder {}", sreq.errors, folderId)
                folderController.view(folderId, req)
            }
            case false => {

                val sub = formService.doSubmission(form, req, files.asScala)
                formService.email(form.recipients.asScala, sub)

                val theme = themeService.default
                val page = ScalaPressPage(theme, ScalapressRequest(req))
                page.body(form.submissionMessage)
                page
            }
        }
    }

}
