package com.cloudray.scalapress.plugin.meta.dublincore

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
class DublinCoreTitleTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext

  val i = new Item
  val sreq = new ScalapressRequest(req, context)
  i.titleTag = "big shirts for big people with big hearts"

  val f = new Folder
  f.titleTag = "buy coldplay tickets"

  val tag = new DublinCoreTitleTag()

  "a DC.Title tag" should "use title from item" in {
    val output = tag.render(sreq.withItem(i), Map.empty)
    assert("<meta name=\"DC.Title\" content=\"big shirts for big people with big hearts\"/>" === output.get)
  }

  "a DC.Title tag" should "use title from folder" in {
    val output = tag.render(sreq.withFolder(f), Map.empty)
    assert("<meta name=\"DC.Title\" content=\"buy coldplay tickets\"/>" === output.get)
  }

  "a DC.Title tag" should "not render when no folder nor item is set" in {
    val output = tag.render(sreq, Map.empty)
    assert(output.isEmpty)
  }

}
