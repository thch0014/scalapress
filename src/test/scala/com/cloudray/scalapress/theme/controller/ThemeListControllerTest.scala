package com.cloudray.scalapress.theme.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.theme.{Theme, ThemeDao}
import org.mockito.{Matchers, Mockito}

/** @author Stephen Samuel */
class ThemeListControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val themeDao = mock[ThemeDao]
  val controller = new ThemeListController(themeDao)

  test("when creating a theme the theme is persisted") {
    controller.create
    Mockito.verify(themeDao).save(Matchers.any[Theme])
  }
}
