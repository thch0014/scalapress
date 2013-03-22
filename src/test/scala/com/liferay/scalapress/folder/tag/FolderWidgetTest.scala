package com.liferay.scalapress.folder.tag

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.folder.{FolderDao, FoldersWidget, Folder}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import org.mockito.Mockito

/** @author Stephen Samuel */
class FolderWidgetTest extends FunSuite with MockitoSugar with BeforeAndAfter {

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext
    context.folderDao = mock[FolderDao]


    test("folder widget happy path") {
        val root = new Folder
        root.id = 1
        root.name = "rootie"

        val child1 = new Folder
        child1.id = 2
        child1.name = "boro"

        val child2 = new Folder
        child2.id = 3
        child2.name = "mogga"

        root.subfolders.add(child1)
        root.subfolders.add(child2)

        Mockito.when(context.folderDao.root).thenReturn(root)

        val render = new FoldersWidget().render(ScalapressRequest(req, context))
        assert(
            <ul class="widget-folder-plugin">
                <li id="w0_f2" class="l1">
                    <a href="/folder-2-boro">boro</a>
                </li>
                <li id='w0_f3' class='l1'>
                    <a href='/folder-3-mogga'>mogga</a>
                </li>
            </ul>.toString().replace("\n", "").replace("\r", "").replaceAll("\\s{2,}", "") === render
              .get
              .replace("\n", "")
              .replace("\r", "")
              .replaceAll("\\s{2,}", ""))
    }
}
