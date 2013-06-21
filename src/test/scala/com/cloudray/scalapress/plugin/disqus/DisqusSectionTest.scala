package com.cloudray.scalapress.plugin.disqus

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressRequest
import org.mockito.Mockito
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.settings.Installation

/** @author Stephen Samuel */
class DisqusSectionTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val section = new DisqusSection
    section.id = 152
    section.shortname = "sammythebull"

    val obj = new Obj
    obj.name = "coldplay tickets"

    val folder = new Folder
    folder.name = "live events"

    val req = mock[ScalapressRequest]
    val installation = mock[Installation]
    Mockito.when(req.installation).thenReturn(installation)
    Mockito.when(installation.domain).thenReturn("buffet.com")

    test("disqus render includes shortname") {
        Mockito.when(req.obj).thenReturn(None)
        Mockito.when(req.folder).thenReturn(None)
        section.render(req).get.contains("var disqus_shortname = 'sammythebull';")
    }

    test("disqus render uses section id for disqus identifer") {
        Mockito.when(req.obj).thenReturn(None)
        Mockito.when(req.folder).thenReturn(None)
        section.render(req).get.contains("var disqus_identifier = '152';")
    }

    test("given an object request then the page title is the name of the object") {
        Mockito.when(req.obj).thenReturn(Option(obj))
        Mockito.when(req.folder).thenReturn(None)
        section.render(req).get.contains("var disqus_title = 'http://buffet.com/coldplay-tickets';")
    }

    test("given a folder request then the page title is the name of the folder") {
        Mockito.when(req.obj).thenReturn(None)
        Mockito.when(req.folder).thenReturn(Option(folder))
        section.render(req).get.contains("var disqus_title = 'http://buffet.com/live-events';")
    }

    test("given an object request then the page url is the url of the object") {
        Mockito.when(req.obj).thenReturn(Option(obj))
        Mockito.when(req.folder).thenReturn(None)
        section.render(req).get.contains("var disqus_url = 'coldplay tickets';")
    }

    test("given a folder request then the page url is the url of the folder") {
        Mockito.when(req.obj).thenReturn(None)
        Mockito.when(req.folder).thenReturn(Option(folder))
        section.render(req).get.contains("var disqus_url = 'live events';")
    }

    test("given a non object/folder request then the page url is the empty string") {
        Mockito.when(req.obj).thenReturn(None)
        Mockito.when(req.folder).thenReturn(None)
        section.render(req).get.contains("var disqus_url = '';")
    }

    test("given a non object/folder request then the page title is the empty string") {
        Mockito.when(req.obj).thenReturn(None)
        Mockito.when(req.folder).thenReturn(None)
        section.render(req).get.contains("var disqus_title = '';")
    }
}
