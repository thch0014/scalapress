package com.cloudray.scalapress.search.widget

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.search.{SearchFormDao, SavedSearchDao}
import org.mockito.Mockito

/** @author Stephen Samuel */
class SearchFormWidgetTest extends FunSuite with OneInstancePerTest with MockitoSugar {

     val context = new ScalapressContext
     context.searchFormDao = mock[SearchFormDao]

     test("init creates and persists a new search form") {
         val widget = new SearchFormWidget
         widget._init(context)
         Mockito.verify(context.searchFormDao).save(widget.searchForm)
     }
 }
