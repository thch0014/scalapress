package com.cloudray.scalapress.theme.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.theme.{Markup, MarkupDao}
import org.mockito.{Matchers, Mockito}

/** @author Stephen Samuel */
class MarkupListControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val controller = new MarkupListController
    controller.markupDao = mock[MarkupDao]

    test("when creating a markup the markup is persisted") {
        controller.create
        Mockito.verify(controller.markupDao).save(Matchers.any[Markup])
    }
}
