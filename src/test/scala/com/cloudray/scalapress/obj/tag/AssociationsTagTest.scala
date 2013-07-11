package com.cloudray.scalapress.obj.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.obj.Obj
import org.mockito.Mockito
import com.cloudray.scalapress.theme.{Markup, MarkupDao}

/** @author Stephen Samuel */
class AssociationsTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val context = mock[ScalapressContext]
  val req = mock[HttpServletRequest]
  val dao = mock[MarkupDao]
  Mockito.when(context.markupDao).thenReturn(dao)

  val m = new Markup
  m.id = 3
  m.body = "[item]"

  Mockito.when(context.markupDao.find(3l)).thenReturn(m)

  val obj = new Obj
  obj.id = 12
  obj.name = "meatballs"
  obj.status = Obj.STATUS_LIVE

  val obj2 = new Obj
  obj2.id = 14
  obj2.name = "spaghetti"
  obj2.status = Obj.STATUS_LIVE

  val obj3 = new Obj
  obj3.id = 14
  obj3.name = "noodles"
  obj3.status = Obj.STATUS_LIVE

  obj.associations.add(obj2)
  obj.associations.add(obj3)

  val sreq = ScalapressRequest(req, context).withObject(obj)

  val tag = new AssociationsTag()

  test("tag renders live items") {
    val actual = tag.render(sreq, Map("markup" -> "3"))
    assert(actual.get.contains("spaghetti"))
    assert(actual.get.contains("noodles"))
  }

  test("tag does not render non live items") {
    obj2.status = Obj.STATUS_DISABLED
    val actual = tag.render(sreq, Map("markup" -> "3"))
    assert(actual.get.contains("noodles"))
  }

  test("tag renders nothing when no associations") {
    obj.associations.clear()
    val actual = tag.render(sreq, Map("markup" -> "3"))
    assert(actual.isEmpty)
  }

  test("tag renders nothing when all associations are not live") {
    obj2.status = Obj.STATUS_DELETED
    obj3.status = Obj.STATUS_DISABLED
    obj.associations.clear()
    val actual = tag.render(sreq, Map("markup" -> "3"))
    assert(actual.isEmpty)
  }
}
