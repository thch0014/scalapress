package com.cloudray.scalapress.theme.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.theme.{Theme, ThemeDao}
import org.mockito.Mockito

/** @author Stephen Samuel */
class ThemeEditControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val controller = new ThemeEditController
    controller.themeDao = mock[ThemeDao]

    test("when saving a theme the theme is persisted") {
        val theme = new Theme
        controller.save(theme)
        Mockito.verify(controller.themeDao).save(theme)
    }
}
