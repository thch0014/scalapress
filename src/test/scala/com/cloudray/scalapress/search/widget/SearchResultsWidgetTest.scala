package com.cloudray.scalapress.search.widget

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.search.SavedSearchDao
import org.mockito.Mockito

/** @author Stephen Samuel */
class SearchResultsWidgetTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = new ScalapressContext
    context.savedSearchDao = mock[SavedSearchDao]

    test("init creates and persists a new search") {
        val widget = new SearchResultsWidget
        widget._init(context)
        Mockito.verify(context.savedSearchDao).save(widget.search)
    }
}
