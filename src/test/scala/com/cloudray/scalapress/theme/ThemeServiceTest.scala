package com.cloudray.scalapress.theme

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.Folder
import org.mockito.Mockito

/** @author Stephen Samuel */
class ThemeServiceTest extends FlatSpec with OneInstancePerTest with MockitoSugar {

  val themeDao = mock[ThemeDao]
  val service = new ThemeService(themeDao)

  val default = new Theme
  Mockito.when(themeDao.findDefault).thenReturn(default)

  "a theme service" should "use folder theme before default" in {
    val folder = new Folder
    folder.theme = new Theme
    assert(folder.theme === service.theme(folder))
  }

  it should "use default theme if no theme is set on the folder" in {
    val folder = new Folder
    assert(default === service.theme(folder))
  }
}
