package com.liferay.scalapress.plugin.form.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.liferay.scalapress.{ScalapressContext, Logging, ScalapressRequest}
import org.springframework.web.multipart.MultipartFile
import com.liferay.scalapress.plugin.form.{RecaptchaClient, Form, FormService, FormDao, SubmissionDao}
import com.liferay.scalapress.theme.{ThemeService, ThemeDao}
import com.liferay.scalapress.folder.controller.FolderController
import com.liferay.scalapress.util.mvc.ScalapressPage
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("form/{id}"))
class SubmissionController extends Logging {

    @Autowired var submissionDao: SubmissionDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var formDao: FormDao = _
    @Autowired var formService: FormService = _
    @Autowired var themeDao: ThemeDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var folderController: FolderController = _

    @ModelAttribute("form") def form(@PathVariable("id") id: Long) = formDao.find(id)

    @ResponseBody
    @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
    def submit(@ModelAttribute("form") form: Form,
               req: HttpServletRequest,
               resp: HttpServletResponse,
               @RequestParam(value = "file") files: java.util.List[MultipartFile],
               @RequestParam(value = "folder", required = false) folderId: Long): ScalapressPage = {

        val sreq = ScalapressRequest(req, context).withTitle("Form Submitted")
        formService.checkErrors(form, sreq)

        if (form.captcha) {
            if (!RecaptchaClient.post(req.getParameter("recaptcha_challenge_field"),
                req.getParameter("recaptcha_response_field"),
                req.getRemoteAddr))
                sreq.error("captcha.error", "Please complete captcha")
        }

        sreq.hasErrors match {
            case true => {
                logger.debug("Form has errors {}, redirecting to folder {}", sreq.errors, folderId)
                folderController.view(folderId, req)
            }
            case false => {

                val submission = formService.doSubmission(form, sreq, files.asScala)
                val theme = themeService.default
                val page = ScalapressPage(theme, sreq)
                page.body(FormSubmissionTextRenderer.render(form.submissionText, submission))
                page
            }
        }
    }

}
