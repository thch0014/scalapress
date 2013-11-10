package com.cloudray.scalapress.search

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class SearchTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val saved = new SavedSearch

  "a search created from a saved search" should "use correct distance" in {
    saved.distance = 100
    assert(Search(saved).distance == 100)
  }

  it should "use correct location when location is not null" in {
    saved.location = "worksop"
    assert(Search(saved).location.get == "worksop")
  }

  it should "use no location when location is null" in {
    saved.location = null
    assert(Search(saved).location.isEmpty)
  }

  it should "use no location when location is empty" in {
    saved.location = " "
    assert(Search(saved).location.isEmpty)
    saved.location = ""
    assert(Search(saved).location.isEmpty)
  }


  it should "trim location when name is not null" in {
    saved.location = "    sheffield   "
    assert(Search(saved).location.get == "sheffield")
  }

  it should "use correct name when name is not null" in {
    saved.name = "coldplay are great"
    assert(Search(saved).name.get == "coldplay are great")
  }

  it should "trim name when name is not null" in {
    saved.name = "    coldplay are great   "
    assert(Search(saved).name.get == "coldplay are great")
  }

  it should "use no name when name is null" in {
    saved.name = null
    assert(Search(saved).name.isEmpty)
  }

  it should "use no name when name is empty" in {
    saved.name = " "
    assert(Search(saved).name.isEmpty)
    saved.name = ""
    assert(Search(saved).name.isEmpty)
  }

  it should "use empty sequence when tags is empty" in {
    saved.labels = " "
    assert(Search(saved).tags.isEmpty)
    saved.labels = ""
    assert(Search(saved).tags.isEmpty)
  }

  it should "trim each tag" in {
    saved.labels = " superman,  batman"
    assert(Search(saved).tags.toSeq(0) === "superman")
    assert(Search(saved).tags.toSeq(1) === "batman")
  }

  it should "trim each folder id" in {
    saved.searchFolders = " 44,  55"
    assert(Search(saved).folders.toSeq(0) === "44")
    assert(Search(saved).folders.toSeq(1) === "55")
  }

  it should "trim each hasAttribute" in {
    saved.hasAttributes = " a44,  b55"
    assert(Search(saved).hasAttributes.toSeq(0) === "a44")
    assert(Search(saved).hasAttributes.toSeq(1) === "b55")
  }
}
