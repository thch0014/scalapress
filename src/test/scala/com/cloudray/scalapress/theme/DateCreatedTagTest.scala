package com.cloudray.scalapress.theme

import org.scalatest.mock.MockitoSugar
import org.scalatest.{OneInstancePerTest, FunSuite}
import tag.DateCreatedTag
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class DateCreatedTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]

  val obj = new Item
  obj.dateCreated = 1364122808957l

  val f = new Folder
  f.dateCreated = 1364122808957l

  test("custom format object happy path") {

    val sreq = new ScalapressRequest(req, context).withItem(obj)
    val rendered = new DateCreatedTag().render(sreq, Map("format" -> "yyyy!MM!dd"))
    assert("2013!03!24" === rendered.get)
  }

  test("custom format folder happy path") {
    val sreq = new ScalapressRequest(req, context).withFolder(f)
    val rendered = new DateCreatedTag().render(sreq, Map("format" -> "yyyy^MM^dd"))
    assert("2013^03^24" === rendered.get)
  }
}
