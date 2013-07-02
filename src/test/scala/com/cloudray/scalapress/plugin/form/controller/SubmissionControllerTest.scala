package com.cloudray.scalapress.plugin.form.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.form.{SubmissionDao, Submission, Form, FormService}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import java.util.Collections
import com.cloudray.scalapress.plugin.form.controller.renderer.FormSubmissionTextRenderer
import com.cloudray.scalapress.theme.ThemeService
import org.mockito.{Matchers, Mockito}
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import scala.collection.mutable
import com.cloudray.scalapress.folder.controller.FolderController
import com.cloudray.scalapress.obj.{ObjectDao, Obj}
import com.cloudray.scalapress.folder.{FolderDao, Folder}
import org.springframework.web.multipart.MultipartFile

/** @author Stephen Samuel */
class SubmissionControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new SubmissionController
    controller.formService = mock[FormService]
    controller.themeService = mock[ThemeService]
    controller.folderController = mock[FolderController]
    controller.submissionDao = mock[SubmissionDao]

    controller.context = new ScalapressContext
    controller.context.folderDao = mock[FolderDao]
    controller.context.objectDao = mock[ObjectDao]

    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]
    val sreq = ScalapressRequest(req, controller.context)
    val form = new Form

    test("submitting a form with no errors creates a submission") {
        Mockito.doReturn(mutable.Map.empty[String, String]).when(req).getAttribute("errors")
        controller.submit(form, req, resp, Collections.emptyList(), 0, 0)
        Mockito
          .verify(controller.formService)
          .doSubmission(Matchers.eq(form), Matchers.any[ScalapressRequest], Matchers.eq(Nil))
    }

    test("when submiting a form then the success page contains the submission scripts") {
        form.submissionScript = "<script>window.alert('west philly born and raised');</script>"
        val page = controller.submit(form, req, resp, Collections.emptyList(), 0, 0)
        assert(page._body.exists(_.toString.contains(form.submissionScript)))
    }

    test("when submitting a form with errors and no folderId was specified then we are forwarded to home page") {
        Mockito.doReturn(mutable.Map("error" -> "yes")).when(req).getAttribute("errors")
        controller.submit(form, req, resp, Collections.emptyList(), 0, 0)
        Mockito.verify(controller.folderController).view(req)
    }

    test("submitting a form with errors does not create a submission and forwards to folderId") {
        Mockito.doReturn(mutable.Map("error" -> "yes")).when(req).getAttribute("errors")
        controller.submit(form, req, resp, Collections.emptyList(), 15, 0)
        Mockito
          .verify(controller.formService, Mockito.never)
          .doSubmission(Matchers.eq(form), Matchers.any[ScalapressRequest], Matchers.eq(Nil))
        Mockito.verify(controller.folderController).view(15, req)
    }

    test("page contains default text is no submission text is set") {
        val page = controller.submit(form, req, resp, Collections.emptyList(), 0, 0)
        assert(page._body.exists(_.toString.contains(FormSubmissionTextRenderer.DEFAULT.toString())))
    }

    test("given a request with an object when creating a submission then the object field is set") {
        val submission = new Submission
        Mockito
          .when(controller
          .formService
          .doSubmission(Matchers.any[Form], Matchers.any[ScalapressRequest], Matchers.any[Iterable[MultipartFile]])).thenReturn(submission)
        val o = new Obj
        Mockito.when(controller.context.objectDao.find(6)).thenReturn(o)
        controller.createSubmission(form, sreq, Nil, 0, 6)
        assert(o === submission.obj)
    }

    test("given a request with a folder when creating a submission then the folder field is set") {
        val submission = new Submission
        Mockito
          .when(controller
          .formService
          .doSubmission(Matchers.any[Form], Matchers.any[ScalapressRequest], Matchers.any[Iterable[MultipartFile]])).thenReturn(submission)
        val f = new Folder
        Mockito.when(controller.context.folderDao.find(5)).thenReturn(f)
        controller.createSubmission(form, sreq, Nil, 5, 0)
        assert(f === submission.folder)
    }
}
