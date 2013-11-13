package com.cloudray.scalapress.plugin.form.section

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.form.Form
import com.cloudray.scalapress.framework.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
class FormSectionTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  val sreq = ScalapressRequest(req, context)
  val section = new FormSection

  "a form section" should "render a form if set" in {
    section.form = new Form
    assert(section.render(sreq).isDefined)
  }

  "a form section" should "not render if no form is set" in {
    section.form = null
    assert(section.render(sreq).isEmpty)
  }
}
