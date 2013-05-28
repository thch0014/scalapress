package com.cloudray.scalapress.plugin.form.renderer

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.form.controller.renderer.FormRenderer
import com.cloudray.scalapress.plugin.form.FormField

/** @author Stephen Samuel */
class FormRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val field = new FormField
    field.id = 124
    field.name = "my field"

    //    test("checkbox rendering happy path") {
    //        assert(
    //            "<div class=\"control-group\"><div class=\"controls\"><label class=\"checkbox\"><input type=\"checkbox\" name=\"124\"></input>my field</label></div></div>" === FormRenderer
    //              ._renderCheck(field).toString)
    //    }

    test("options rendering happy path") {
        assert(
            Array(<option>coldplay</option>, <option>jethro tull</option>) === FormRenderer
              ._renderOptions(Array("coldplay", "jethro tull")).toArray)
    }

    test("selection rendering happy path") {
        val req = mock[HttpServletRequest]
        val context = new ScalapressContext
        val sreq = ScalapressRequest(req, context)
        field.options = "coldplay,jethro tull"
        assert(
            "<div class=\"control-group\"><label class=\"control-label\">my field </label><div class=\"controls\"><select name=\"124\"><option>coldplay,jethro tull</option></select><span class=\"help-inline\"></span></div></div>" === FormRenderer
              ._renderSelect(field, sreq).toString)
    }
}
