package com.cloudray.scalapress.plugin.search.wizard

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.framework.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.search._
import org.mockito.{Matchers, Mockito}
import com.cloudray.scalapress.search.SearchResult
import com.cloudray.scalapress.search.Facet
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.item.ItemDao
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
class WizardSectionTest extends FlatSpec with OneInstancePerTest with MockitoSugar {

  val section = new WizardSection
  section.search = new SavedSearch
  section.search.name = "spiderman"

  val step1 = WizardStep("choose your hero", null, 1l)
  val step2 = WizardStep("where does she/he live", null, 2l)
  val step3 = WizardStep("what special powers huh", null, 3l)

  section.steps = List(step1, step2).asJava

  val req = mock[HttpServletRequest]
  Mockito.when(req.getRequestURL).thenReturn(new StringBuffer("http://www.domain.com"))

  val context = mock[ScalapressContext]
  val itemDao = mock[ItemDao]
  Mockito.when(context.itemDao).thenReturn(itemDao)
  Mockito.when(context.itemDao.findBulk(Matchers.any[Seq[Long]])).thenReturn(Nil)

  val f = new Folder
  f.name = "comics"
  f.id = 14
  val sreq = ScalapressRequest(req, context).withFolder(f)

  val searchService = mock[SearchService]
  Mockito.when(context.bean[SearchService]).thenReturn(searchService)

  val facet1 = Facet("superhero", AttributeFacetField(1), Seq(FacetTerm("superman", 3), FacetTerm("batman", 4)))
  val facet2 = Facet("location", AttributeFacetField(2), Seq(FacetTerm("gotham", 2), FacetTerm("metropolis", 9)))
  val facet3 = Facet("powers", AttributeFacetField(3), Seq(FacetTerm("xray", 7), FacetTerm("batbelt", 1)))

  "a wizard section" should "show only the first facet when no facets are selected" in {

    Mockito.when(req.getRequestURL).thenReturn(new StringBuffer("http://www.domain.com"))

    val result = SearchResult(facets = Seq(facet1, facet2, facet3))
    Mockito.when(searchService.search(Matchers.any[Search])).thenReturn(result)

    val output = section.render(sreq).get
    assert(output.contains("choose your hero"))
    assert(output.contains("superman"))
    assert(output.contains("batman"))
    assert(!output.contains("location"))
    assert(!output.contains("where does she/he live"))
    assert(!output.contains("gotham"))
    assert(!output.contains("metropolis"))
  }

  it should "show the next facet when a facet is selected" in {

    Mockito.when(req.getQueryString).thenReturn("afacet_1=superman")

    val result = SearchResult(facets = Seq(facet1, facet2, facet3))
    Mockito.when(searchService.search(Matchers.any[Search])).thenReturn(result)

    val output = section.render(sreq).get
    assert(!output.contains("choose your hero"))
    assert(output.contains("where does she/he live"))
    assert(output.contains("gotham"))
    assert(output.contains("metropolis"))
    assert(!output.contains("what special powers huh"))
    assert(!output.contains("batbelt"))
  }
}
