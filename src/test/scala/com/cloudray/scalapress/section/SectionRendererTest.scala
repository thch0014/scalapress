package com.cloudray.scalapress.section

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.{ItemType, Item}

/** @author Stephen Samuel */
class SectionRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]
  val sreq = ScalapressRequest(req, context)

  val section1 = new StringSection("kirk")
  section1.visible = true
  section1.position = 99

  val section2 = new StringSection("kaaaan")
  section2.visible = true
  section2.position = 14

  test("renderer only includes visible sections") {
    section2.visible = false
    val rendered = SectionRenderer._render(Seq(section1, section2), sreq)
    assert(rendered.contains("kirk"))
    assert(!rendered.contains("kaaaan"))
  }

  test("rendered for objects includes sections from item type") {
    val obj = new Item
    obj.sections.add(section1)

    obj.objectType = new ItemType
    obj.objectType.sections.add(section2)

    val output = SectionRenderer.render(obj, sreq)
    assert(output.contains("kirk"))
    assert(output.contains("kaaaan"))
  }

  test("renderer sorts by section position") {
    val rendered = SectionRenderer._render(Seq(section2, section1), sreq)
    assert("(?s).*kaaaan.*kirk.*".r.findFirstIn(rendered).isDefined)
  }
}

class StringSection(val string: String) extends Section {
  def render(request: ScalapressRequest): Option[String] = Some(string)
  def desc: String = "StringSection"
}