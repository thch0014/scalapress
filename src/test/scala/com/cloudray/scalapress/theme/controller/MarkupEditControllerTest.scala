package com.cloudray.scalapress.theme.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.theme.{Markup, MarkupDao}
import org.mockito.Mockito

/** @author Stephen Samuel */
class MarkupEditControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val controller = new MarkupEditController
    controller.markupDao = mock[MarkupDao]

    test("when saving a markup the markup is persisted") {
        val markup = new Markup
        controller.save(markup)
        Mockito.verify(controller.markupDao).save(markup)
    }
}
