package com.cloudray.scalapress.obj.controller.admin

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.theme.{ThemeDao, Theme}

/** @author Stephen Samuel */
class ThemePopulatorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val t1 = new Theme
  t1.id = 3
  t1.name = "Murdock"

  val t2 = new Theme
  t2.id = 4
  t2.name = "Face man"

  val t3 = new Theme
  t3.id = 2
  t3.name = "hannibal"

  val populator = new ThemePopulator {
    val themeDao: ThemeDao = mock[ThemeDao]
  }

  Mockito.when(populator.themeDao.findAll).thenReturn(List(t1, t2, t3))

  test("that themes are populated in order") {
    val model = new ModelMap
    populator.themes(model)
    val themes = model.get("themesMap").asInstanceOf[java.util.Map[String, String]]
    assert(4 === themes.size)
    val it = themes.entrySet().iterator()
    assert("-None-" === it.next().getValue)
    assert("#2 hannibal" === it.next().getValue)
    assert("#3 Murdock" === it.next().getValue)
    assert("#4 Face man" === it.next().getValue)
  }

}
