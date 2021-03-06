package com.cloudray.scalapress.search.widget

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.search.SearchFormDao
import org.mockito.Mockito
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
class SearchFormWidgetTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val context = new ScalapressContext
  context.searchFormDao = mock[SearchFormDao]

  test("init creates and persists a new search form") {
    val widget = new SearchFormWidget
    widget._init(context)
    Mockito.verify(context.searchFormDao).save(widget.searchForm)
  }

  test("backoffice url is absolute") {
    val widget = new SearchFormWidget
    assert(widget.backoffice.startsWith("/backoffice/"))
  }
}
