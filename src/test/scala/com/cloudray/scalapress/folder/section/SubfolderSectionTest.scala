package com.cloudray.scalapress.folder.section

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.{FolderSettings, FolderPluginDao, Folder}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.theme.Markup
import org.mockito.Mockito
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class SubfolderSectionTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val folder2 = new Folder
    folder2.id = 2
    folder2.name = "visible"

    val folder3 = new Folder
    folder3.id = 3
    folder3.name = "hidden"
    folder3.hidden = true

    val root = new Folder
    root.id = 1
    root.subfolders.add(folder2)
    root.subfolders.add(folder3)

    val section = new SubfolderSection()
    section.folder = root

    val context = new ScalapressContext
    context.folderSettingsDao = mock[FolderPluginDao]
    val req = ScalapressRequest(mock[HttpServletRequest], context)
    Mockito.when(context.folderSettingsDao.head).thenReturn(new FolderSettings)

    test("render uses supplied markup when set") {
        section.markup = new Markup
        section.markup.body = "[folder]"
        section.markup.start = "<p>"
        section.markup.end = "</p>"
        val rendered = section.render(req).get
        assert( """<p>visible</p>""" === rendered)
    }

    test("render uses default markup when non specified") {
        val rendered = section.render(req).get
        assert( """<ul><li><a href="/folder-2-visible">visible</a></li></ul>""" === rendered)
    }

    test("that hidden folders are not included") {
        val subfolders = section._folders
        assert(subfolders.size === 1)
        assert(subfolders(0) === folder2)
    }
}
