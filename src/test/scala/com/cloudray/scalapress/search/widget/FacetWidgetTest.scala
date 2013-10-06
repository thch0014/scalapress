package com.cloudray.scalapress.search.widget

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.search._
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.folder.Folder
import javax.servlet.http.HttpServletRequest
import org.mockito.{Mockito, Matchers}
import com.cloudray.scalapress.search.SearchResult
import com.cloudray.scalapress.search.Facet
import org.apache.commons.io.IOUtils
import scala.xml.{XML, Utility}

/** @author Stephen Samuel */
class FacetWidgetTest extends FlatSpec with OneInstancePerTest with MockitoSugar {

  val context = new ScalapressContext
  val folder = new Folder
  val sreq = ScalapressRequest(mock[HttpServletRequest], context).withFolder(folder)

  val widget = new FacetWidget
  widget.service = mock[SearchService]

  "a facet widget" should "render nothing when not in a folder" in {
    val sreq = ScalapressRequest(mock[HttpServletRequest], context)
    assert(widget.render(sreq).isEmpty)
  }

  "a facet widget" should "render nothing when no facets are found" in {
    Mockito.when(widget.service.search(Matchers.any[SavedSearch])).thenReturn(new SearchResult)
    assert(widget.render(sreq).isEmpty)
  }

  "a facet widget" should "render all facets" in {

    val facet1 = Facet("teams", "places", Seq(FacetTerm("Boro", 4), FacetTerm("Toon", 2)))
    val facet2 = Facet("Colours", "places", Seq(FacetTerm("red", 10), FacetTerm("blue", 20)))

    val result = new SearchResult(facets = Seq(facet1, facet2))
    Mockito.when(widget.service.search(Matchers.any[SavedSearch])).thenReturn(result)

    val html = widget.render(sreq)
    val xml1 = Utility.trim(XML.loadString(html.get))
    val expected = IOUtils.toString(getClass.getResourceAsStream("/com/cloudray/scalapress/search/widget/facet1.html"))
    val xml2 = Utility.trim(XML.loadString(expected))
    assert(xml1 === xml2)
  }
}
