package com.liferay.scalapress.plugin.form

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
class FormRendererTest extends FunSuite with MockitoSugar {

    val field = new FormField
    field.id = 124
    field.name = "my field"

    test("checkbox rendering happy path") {
        assert(
            "<div class=\"control-group\"><div class=\"controls\"><label class=\"checkbox\"><input name=\"124\" type=\"checkbox\"></input>my field</label></div></div>" === FormRenderer
              ._renderCheck(field).toString)
    }

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
