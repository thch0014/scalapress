package com.cloudray.scalapress.plugin.meta.dublincore

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
class DublinCoreSubjectTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext

  val i = new Item
  val sreq = new ScalapressRequest(req, context)
  i.keywordsTag = "coldplay live chris will band yellow clocks"

  val f = new Folder
  f.keywordsTag = "boro karanka mfc gibson juninho"

  val tag = new DublinCoreSubjectTag()

  "a DC.Subject tag" should "use keywords from item" in {
    val output = tag.render(sreq.withItem(i), Map.empty)
    assert("<meta name=\"DC.Subject\" content=\"coldplay live chris will band yellow clocks\"/>" === output.get)
  }

  "a DC.Subject tag" should "use keywords from folder" in {
    val output = tag.render(sreq.withFolder(f), Map.empty)
    assert("<meta name=\"DC.Subject\" content=\"boro karanka mfc gibson juninho\"/>" === output.get)
  }

  "a DC.Subject tag" should "not render when no folder nor item is set" in {
    val output = tag.render(sreq, Map.empty)
    assert(output.isEmpty)
  }

}
