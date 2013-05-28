package com.cloudray.scalapress.plugin.form.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.form.{Form, FormService}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import java.util.Collections
import com.cloudray.scalapress.plugin.form.controller.renderer.FormSubmissionTextRenderer
import com.cloudray.scalapress.theme.ThemeService
import org.mockito.{Matchers, Mockito}
import com.cloudray.scalapress.ScalapressRequest
import scala.collection.mutable
import com.cloudray.scalapress.folder.controller.FolderController

/** @author Stephen Samuel */
class SubmissionControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new SubmissionController
    controller.formService = mock[FormService]
    controller.themeService = mock[ThemeService]
    controller.folderController = mock[FolderController]
    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]

    val form = new Form

    test("submitting a form with no errors creates a submission") {
        Mockito.doReturn(mutable.Map.empty[String, String]).when(req).getAttribute("errors")
        controller.submit(form, req, resp, Collections.emptyList(), 0)
        Mockito.verify(controller.formService).doSubmission(Matchers.eq(form), Matchers.any[ScalapressRequest], Matchers.eq(Nil))
    }

    test("when submiting a form then the success page contains the submission scripts") {
        form.submissionScript = "<script>window.alert('west philly born and raised');</script>"
        val page = controller.submit(form, req, resp, Collections.emptyList(), 0)
        assert(page._body.exists(_.toString.contains(form.submissionScript)))
    }

    test("when submitting a form with errors and no folderId was specified then we are forwarded to home page") {
        Mockito.doReturn(mutable.Map("error" -> "yes")).when(req).getAttribute("errors")
        controller.submit(form, req, resp, Collections.emptyList(), 0)
        Mockito.verify(controller.folderController).view(req)
    }

    test("submitting a form with errors does not create a submission and forwards to folderId") {
        Mockito.doReturn(mutable.Map("error" -> "yes")).when(req).getAttribute("errors")
        controller.submit(form, req, resp, Collections.emptyList(), 15)
        Mockito
          .verify(controller.formService, Mockito.never)
          .doSubmission(Matchers.eq(form), Matchers.any[ScalapressRequest], Matchers.eq(Nil))
        Mockito.verify(controller.folderController).view(15, req)
    }

    test("page contains default text is no submission text is set") {
        val page = controller.submit(form, req, resp, Collections.emptyList(), 0)
        assert(page._body.exists(_.toString.contains(FormSubmissionTextRenderer.DEFAULT.toString())))
    }
}
