package com.cloudray.scalapress.theme.tag

import org.scalatest.mock.MockitoSugar
import org.scalatest.{OneInstancePerTest, FunSuite}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class DescriptionTagTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]

  val obj = new Item
  obj.dateCreated = 1364122808957l
  obj.descriptionTag = "I love descriptions me"

  val f = new Folder
  f.dateCreated = 1364122808957l
  f.descriptionTag = "folders have the best descriptions"


  test("description tag uses desc field of object") {
    val sreq = new ScalapressRequest(req, context).withItem(obj)
    val rendered = new DescriptionTagTag().render(sreq, Map.empty)
    assert("<meta name='description' content='I love descriptions me'/>" === rendered.get)
  }

  test("description tag uses desc field of folder") {
    val sreq = new ScalapressRequest(req, context).withFolder(f)
    val rendered = new DescriptionTagTag().render(sreq, Map.empty)
    assert("<meta name='description' content='folders have the best descriptions'/>" === rendered.get)
  }
}
