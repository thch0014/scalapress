package com.cloudray.scalapress.folder.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.{FolderSettings, FolderPluginDao, Folder, FolderDao}
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito
import com.cloudray.scalapress.theme.ThemeService

/** @author Stephen Samuel */
class FolderControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new FolderController
    controller.folderDao = mock[FolderDao]
    controller.folderPluginDao = mock[FolderPluginDao]
    controller.themeService = mock[ThemeService]

    val settings = new FolderSettings
    Mockito.when(controller.folderPluginDao.head).thenReturn(settings)

    val req = mock[HttpServletRequest]

    test("the rendered page includes the header") {
        val folder = new Folder
        folder.header = "lovely header"
        val page = controller.view(folder, req)
        assert(page.render.contains("lovely head"))
    }

    test("the rendered page includes the footer") {
        val folder = new Folder
        folder.footer = "big feet"
        val page = controller.view(folder, req)
        assert(page.render.contains("big feet"))
    }

    test("the rendered page includes the header from settings if folder header is null") {
        val folder = new Folder
        settings.header = "super header"
        val page = controller.view(folder, req)
        assert(page.render.contains("super header"))
    }

    test("the rendered page includes the footer from settings if folder footer is null") {
        val folder = new Folder
        settings.footer = "general bigfoot"
        val page = controller.view(folder, req)
        assert(page.render.contains("general bigfoot"))
    }
}
