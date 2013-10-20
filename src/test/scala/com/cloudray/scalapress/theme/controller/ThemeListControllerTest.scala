package com.cloudray.scalapress.theme.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.theme.{ThemeImporter, Theme, ThemeDao}
import org.mockito.{Matchers, Mockito}

/** @author Stephen Samuel */
class ThemeListControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val themeDao = mock[ThemeDao]
  val themeImporter = mock[ThemeImporter]
  val controller = new ThemeListController(themeDao, themeImporter)

  test("when creating a theme the theme is persisted") {
    controller.create
    Mockito.verify(themeDao).save(Matchers.any[Theme])
  }
}
