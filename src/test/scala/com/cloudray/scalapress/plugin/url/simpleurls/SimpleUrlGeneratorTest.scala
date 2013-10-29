package com.cloudray.scalapress.plugin.url.simpleurls

import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.Folder
import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class SimpleUrlGeneratorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  test("object friendly url happy path") {
    val obj = new Item
    obj.id = 1234
    obj.name = "boro for the champo"
    assert("/o1234-boro-for-the-champo" === SimpleUrlGeneratorStrategy.url(obj))
  }

  test("folder friendly url happy path") {
    val f = new Folder
    f.id = 55
    f.name = "uefa cup final 2006"
    assert("/f55-uefa-cup-final-2006" === SimpleUrlGeneratorStrategy.url(f))
  }
}
