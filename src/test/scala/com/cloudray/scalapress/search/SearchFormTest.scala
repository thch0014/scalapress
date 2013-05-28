package com.cloudray.scalapress.search

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
class SearchFormTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val field1 = new SearchFormField
    field1.id = 1
    field1.position = 3
    field1.name = "jethro tull"
    val field2 = new SearchFormField
    field2.id = 2
    field2.position = 9
    field2.name = "coldplay"
    val field3 = new SearchFormField
    field3.id = 3
    field3.position = 6
    field3.name = "kate bush"

    val form = new SearchForm
    form.fields.add(field1)
    form.fields.add(field2)
    form.fields.add(field3)

    test("sorted fields is stable with respect to position") {
        for ( field <- form.fields.asScala ) field.position = 0
        for ( i <- 1 to 10 ) {
            val sorted = form.sortedFields
            assert(sorted.size === 3)
            assert(sorted(0).id === 2)
            assert(sorted(1).id === 1)
            assert(sorted(2).id === 3)
        }
    }

    test("sorted fields orders by position") {

        val sorted = form.sortedFields
        assert(sorted.size === 3)
        assert(sorted(0).id === 1)
        assert(sorted(1).id === 3)
        assert(sorted(2).id === 2)

    }

}
