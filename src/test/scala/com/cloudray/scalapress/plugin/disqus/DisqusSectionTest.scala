package com.cloudray.scalapress.plugin.disqus

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressRequest
import org.mockito.Mockito
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.settings.Installation

/** @author Stephen Samuel */
class DisqusSectionTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val section = new DisqusSection
  section.id = 152
  section.shortname = "sammythebull"

  val obj = new Item
  obj.id = 34
  obj.name = "coldplay tickets"

  val folder = new Folder
  folder.name = "live events"

  val req = mock[ScalapressRequest]
  val installation = mock[Installation]
  Mockito.when(req.installation).thenReturn(installation)
  Mockito.when(installation.domain).thenReturn("buffet.com")

  test("disqus render includes shortname") {
    Mockito.when(req.item).thenReturn(None)
    Mockito.when(req.folder).thenReturn(None)
    assert(section.render(req).get.contains( """var disqus_shortname = "sammythebull";"""))
  }

  test("disqus render uses section and object id for disqus identifer") {
    Mockito.when(req.item).thenReturn(None)
    Mockito.when(req.folder).thenReturn(None)
    assert(section.render(req).get.contains( """var disqus_identifier = "152";"""))
  }

  test("given an object sreq then the page title is the name of the object") {
    Mockito.when(req.item).thenReturn(Option(obj))
    Mockito.when(req.folder).thenReturn(None)
    assert(section.render(req).get.contains( """var disqus_title = "coldplay tickets";"""))
  }

  test("given a folder sreq then the page title is the name of the folder") {
    Mockito.when(req.item).thenReturn(None)
    Mockito.when(req.folder).thenReturn(Option(folder))
    assert(section.render(req).get.contains( """var disqus_title = "live events";"""))
  }

  test("given an object sreq then the page url is the url of the object") {
    Mockito.when(req.item).thenReturn(Option(obj))
    Mockito.when(req.folder).thenReturn(None)
    assert(section.render(req).get.contains( """var disqus_url = "http://buffet.com/object-34-coldplay-tickets";"""))
  }

  test("given a folder sreq then the page url is the url of the folder") {
    Mockito.when(req.item).thenReturn(None)
    Mockito.when(req.folder).thenReturn(Option(folder))
    assert(section.render(req).get.contains( """var disqus_url = "http://buffet.com/folder-0-live-events";"""))
  }

  test("given a non object/folder sreq then the page url is the empty string") {
    Mockito.when(req.item).thenReturn(None)
    Mockito.when(req.folder).thenReturn(None)
    assert(section.render(req).get.contains( """var disqus_url = "";"""))
  }

  test("given a non object/folder sreq then the page title is the empty string") {
    Mockito.when(req.item).thenReturn(None)
    Mockito.when(req.folder).thenReturn(None)
    assert(section.render(req).get.contains( """var disqus_title = "";"""))
  }

  test("an object's title should remove quote marks") {
    obj.name = "coldplay are \"great\""
    Mockito.when(req.item).thenReturn(Option(obj))
    Mockito.when(req.folder).thenReturn(None)
    println(section.render(req))
    assert(section.render(req).get.contains( """var disqus_title = "coldplay are 'great'";"""))
  }
}
