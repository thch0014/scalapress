package com.cloudray.scalapress.item.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
class LinkTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val context = new ScalapressContext()
  val req = mock[HttpServletRequest]

  val item = new Item
  item.id = 12
  item.name = "meatballs"

  val f = new Folder
  f.id = 435
  f.name = "italian foods"

  test("item is used if set") {
    val sreq = ScalapressRequest(req, context).withItem(item)
    val actual = LinkTag.render(sreq, Map.empty).get
    assert("/item-12-meatballs" === actual)
  }

  test("folder is used if set") {
    val sreq = ScalapressRequest(req, context).withFolder(f)
    val actual = LinkTag.render(sreq, Map.empty).get
    assert("/folder-435-italian-foods" === actual)
  }
}
