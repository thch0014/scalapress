package com.cloudray.scalapress.folder.controller

import org.scalatest._
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.mockito.Mockito
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.media.AssetStore

/** @author Stephen Samuel */
class FolderControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest with ShouldMatchers {

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

  val folder = new Folder
  folder.name = "big super smashing lovely folder"
  folder.header = "lovely header"
  folder.footer = "big feet"

  settings.header = "super header"
  settings.footer = "general bigfoot"

  val interceptor1 = mock[FolderInterceptor]
  val interceptor2 = mock[FolderInterceptor]
  Mockito.when(interceptor1.preHandle(folder, req, resp)).thenReturn(true)
  Mockito.when(interceptor2.preHandle(folder, req, resp)).thenReturn(true)

  val interceptors = List(interceptor1, interceptor2)
  Mockito.when(context.beans[FolderInterceptor]).thenReturn(interceptors)

  "a folder controller" should "include the header in the page" in {
    val page = controller.view(folder, req, resp)
    assert(page.render.contains("lovely head"))
  }

  it should "include the footer in the page" in {
    val page = controller.view(folder, req, resp)
    assert(page.render.contains("big feet"))
  }

  it should "include the header from settings if folder header is null" in {
    folder.header = null
    val page = controller.view(folder, req, resp)
    assert(page.render.contains("super header"))
  }

  it should "include the footer from settings if folder footer is null" in {
    folder.footer = null
    val page = controller.view(folder, req, resp)
    assert(page.render.contains("general bigfoot"))
  }

  it should "call all pre handling interceptors" in {
    controller.view(folder, req, resp)
    Mockito.verify(interceptor1).preHandle(folder, req, resp)
    Mockito.verify(interceptor2).preHandle(folder, req, resp)
  }

  it should "call all post handling interceptors" in {
    controller.view(folder, req, resp)
    Mockito.verify(interceptor1).postHandle(folder, req, resp)
    Mockito.verify(interceptor2).postHandle(folder, req, resp)
  }

  it should "stop execution if an interceptor pre call returns false" in {
    Mockito.when(interceptor1.preHandle(folder, req, resp)).thenReturn(false)
    evaluating {
      controller.view(folder, req, resp)
    } should produce[FolderInterceptorException]
  }

  it should "set title when hideTitle is false" in {
    folder.hideTitle = false
    val page = controller.view(folder, req, resp)
    assert(page.sreq.title.get === "big super smashing lovely folder")
  }

  it should "not set title when hideTitle is true" in {
    folder.hideTitle = true
    val page = controller.view(folder, req, resp)
    assert(page.sreq.title.isEmpty)
  }
}
