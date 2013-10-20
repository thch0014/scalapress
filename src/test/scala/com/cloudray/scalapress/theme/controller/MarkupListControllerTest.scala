package com.cloudray.scalapress.theme.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.theme.{Markup, MarkupDao}
import org.mockito.{Matchers, Mockito}

/** @author Stephen Samuel */
class MarkupListControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val markupDao = mock[MarkupDao]
  val controller = new MarkupListController(markupDao)

  test("when creating a markup the markup is persisted") {
    controller.create
    Mockito.verify(markupDao).save(Matchers.any[Markup])
  }
}
