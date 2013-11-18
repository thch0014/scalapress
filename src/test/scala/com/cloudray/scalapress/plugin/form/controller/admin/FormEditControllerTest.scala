package com.cloudray.scalapress.plugin.form.controller.admin

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.mockito.Mockito
import com.cloudray.scalapress.plugin.form.{FormDao, SubmissionDao, Form}
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
class FormEditControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val submissionDao = mock[SubmissionDao]
  val formDao = mock[FormDao]
  val context = new ScalapressContext
  val controller = new FormEditController(submissionDao, formDao, context)
  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]

  val form = new Form

  "a form edit controller" should "return 200 on re-order" in {
    controller.reorderFields(form, "4-15-6", resp)
    Mockito.verify(resp).setStatus(200)
  }

}
