package com.cloudray.scalapress.search.widget

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class FacetWidgetTest extends FlatSpec with OneInstancePerTest with MockitoSugar {

//  val context = new ScalapressContext
  //  val folder = new Folder
  //  val sreq = ScalapressRequest(mock[HttpServletRequest], context).withFolder(folder)
  //
  //  val widget = new FacetWidget
  //  widget.attributes = "1,2"
  //  widget.service = mock[SearchService]
  //
  //  "a facet widget" should "render nothing when not in a folder" in {
  //    val sreq = ScalapressRequest(mock[HttpServletRequest], context)
  //    assert(widget.render(sreq).isEmpty)
  //  }
  //
  //  "a facet widget" should "render nothing when no facets are found" in {
  //    Mockito.when(widget.service.search(Matchers.any[SavedSearch])).thenReturn(new SearchResult)
  //    assert(widget.render(sreq).isEmpty)
  //  }
  //
  //  "a facet widget" should "render all facets" in {
  //
  //    val facet1 = Facet("teams", AttributeFacetField(1), Seq(FacetTerm("Boro", 4), FacetTerm("Toon", 2)))
  //    val facet2 = Facet("Colours", AttributeFacetField(2), Seq(FacetTerm("red", 10), FacetTerm("blue", 20)))
  //
  //    val result = new SearchResult(facets = Seq(facet1, facet2))
  //    Mockito.when(widget.service.search(Matchers.any[SavedSearch])).thenReturn(result)
  //
  //    val html = widget.render(sreq)
  //    val xml1 = Utility.trim(XML.loadString(html.get))
  //    val expected = IOUtils.toString(getClass.getResourceAsStream("/com/cloudray/scalapress/search/widget/facet1.html"))
  //    val xml2 = Utility.trim(XML.loadString(expected))
  //    assert(xml1 === xml2)
  //  }
  //
  //  "a facet widget" should "use the attributes field to select facets" in {
  //
  //    widget.attributes = "1,5,9"
  //    val captor = ArgumentCaptor.forClass(classOf[SavedSearch])
  //    Mockito.when(widget.service.search(Matchers.any[SavedSearch])).thenReturn(new SearchResult)
  //
  //    widget.render(sreq)
  //    Mockito.verify(widget.service).search(captor.capture)
  //    assert(3 === captor.getValue.facets.size)
  //    assert(captor.getValue.facets.toSeq.contains("1"))
  //    assert(captor.getValue.facets.toSeq.contains("5"))
  //    assert(captor.getValue.facets.toSeq.contains("9"))
  //  }
}
