package com.cloudray.scalapress.search.controller.admin

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.search.{SearchFormDao, SearchForm, SearchFormField}
import org.mockito.Mockito

/** @author Stephen Samuel */
class SearchFormEditControllerTest extends FlatSpec with OneInstancePerTest with MockitoSugar {

    val controller = new SearchFormEditController
    val form = new SearchForm

    val field1 = new SearchFormField
    field1.name = "sammy"
    field1.position = 2
    form.fields.add(field1)

    val field2 = new SearchFormField
    field2.name = "dean"
    field2.position = 2
    form.fields.add(field2)

    val field3 = new SearchFormField
    field3.name = "dean"
    field3.position = 3
    form.fields.add(field3)

    controller.searchFormDao = mock[SearchFormDao]
    Mockito.when(controller.searchFormDao.find(4)).thenReturn(form)

    "A search controller" should "use sorted fields in the model" in {
        val fields = controller.fields(4)
        assert(3 === fields.size)
        assert(field2 === fields(0))
        assert(field1 === fields(1))
        assert(field3 === fields(2))
    }
}
