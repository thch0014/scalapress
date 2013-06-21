package com.cloudray.scalapress.theme.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.theme.{Theme, ThemeDao}
import org.mockito.{Matchers, Mockito}

/** @author Stephen Samuel */
class ThemeListControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val controller = new ThemeListController
    controller.themeDao = mock[ThemeDao]

    test("when creating a theme the theme is persisted") {
        controller.create
        Mockito.verify(controller.themeDao).save(Matchers.any[Theme])
    }
}
