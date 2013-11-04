package com.cloudray.scalapress.plugin.form.renderer

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.form.controller.renderer.FormRenderer
import com.cloudray.scalapress.plugin.form.{Form, FormField}
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

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

  val form = new Form
  form.id = 124
  form.name = "sammyform"

  val context = new ScalapressContext
  val sreq = ScalapressRequest(mock[HttpServletRequest], context)

  test("given an object page then the form action includes the object id") {
    val o = new Item
    o.id = 124
    val action = FormRenderer.action(form, sreq.withItem(o))
    assert("/form/124?folderId=0&objId=124" === action)
  }

  test("given a folder page then the form action includes the folder id") {
    val f = new Folder
    f.id = 23434
    val action = FormRenderer.action(form, sreq.withFolder(f))
    assert("/form/124?folderId=23434&objId=0" === action)
  }

  test("options rendering happy path") {
    assert(
      Array(<option>coldplay</option>, <option>jethro tull</option>) === FormRenderer
        ._renderOptions(Array("coldplay", "jethro tull")).toArray)
  }

}
