package com.cloudray.scalapress.theme.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.theme.{Theme, ThemeDao}
import org.mockito.Mockito

/** @author Stephen Samuel */
class ThemeEditControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val themeDao = mock[ThemeDao]
  val controller = new ThemeEditController(themeDao)

  test("when saving a theme the theme is persisted") {
    val theme = new Theme
    controller.save(theme)
    Mockito.verify(themeDao).save(theme)
  }
}
