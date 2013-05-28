package com.cloudray.scalapress.plugin.form.renderer

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.form.controller.renderer.FormSubmissionTextRenderer
import com.cloudray.scalapress.plugin.form.Submission

/** @author Stephen Samuel */
class FormSubmissionTextRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val submission = new Submission

    test("if no text is set then use the default") {
        val rendered = FormSubmissionTextRenderer.render(null, submission)
        assert("<p>Thank you for your submission. We will respond as soon as possible.</p>" === rendered)
    }
}
