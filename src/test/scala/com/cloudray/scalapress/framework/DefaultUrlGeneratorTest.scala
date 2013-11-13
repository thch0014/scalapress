package com.cloudray.scalapress.framework

import org.scalatest.FlatSpec
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.item.Item

/** @author Stephen Samuel */
class DefaultUrlGeneratorTest extends FlatSpec with MockitoSugar {

  "a default url generator" should "use simple url format for folders" in {
    val folder = new Folder
    folder.id = 39
    folder.name = "coldplay tickets"
    val url = DefaultUrlStrategy.url(folder)
    assert("/folder/39" === url)
  }

  it should "use simple url format for items" in {
    val item = new Item
    item.id = 65
    item.name = "big man t shirt"
    val url = DefaultUrlStrategy.url(item)
    assert("/item/65" === url)
  }
}
