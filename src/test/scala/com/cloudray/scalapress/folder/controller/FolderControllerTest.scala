package com.cloudray.scalapress.folder.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.mockito.Mockito
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.media.AssetStore

/** @author Stephen Samuel */
class FolderControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val folderDao = mock[FolderDao]
  val folderPluginDao = mock[FolderPluginDao]
  val themeService = mock[ThemeService]
  val context = mock[ScalapressContext]
  val controller = new FolderController(folderDao, folderPluginDao, context, themeService)

  val assetStore = mock[AssetStore]
  Mockito.when(context.assetStore).thenReturn(assetStore)
  Mockito.when(assetStore.baseUrl).thenReturn("baseurl.com")
  Mockito.when(context.beans[FolderInterceptor]).thenReturn(Nil)

  val settings = new FolderSettings
  Mockito.when(folderPluginDao.head).thenReturn(settings)

  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]

  test("the rendered page includes the header") {
    val folder = new Folder
    folder.header = "lovely header"
    val page = controller.view(folder, req, resp)
    assert(page.render.contains("lovely head"))
  }

  test("the rendered page includes the footer") {
    val folder = new Folder
    folder.footer = "big feet"
    val page = controller.view(folder, req, resp)
    assert(page.render.contains("big feet"))
  }

  test("the rendered page includes the header from settings if folder header is null") {
    val folder = new Folder
    settings.header = "super header"
    val page = controller.view(folder, req, resp)
    assert(page.render.contains("super header"))
  }

  test("the rendered page includes the footer from settings if folder footer is null") {
    val folder = new Folder
    settings.footer = "general bigfoot"
    val page = controller.view(folder, req, resp)
    assert(page.render.contains("general bigfoot"))
  }
}
