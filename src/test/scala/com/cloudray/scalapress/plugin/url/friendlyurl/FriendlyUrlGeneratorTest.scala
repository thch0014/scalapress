package com.cloudray.scalapress.plugin.url.friendlyurl

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.framework.UrlGenerator

/** @author Stephen Samuel */
class FriendlyUrlGeneratorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  test("item friendly url happy path") {
    val item = new Item
    item.id = 1234
    item.name = "boro for the champo"
    assert("/item-1234-boro-for-the-champo" === UrlGenerator.url(item))
  }

  test("folder friendly url happy path") {
    val f = new Folder
    f.id = 55
    f.name = "uefa cup final 2006"
    assert("/folder-55-uefa-cup-final-2006" === UrlGenerator.url(f))
  }
}
