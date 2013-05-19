package com.liferay.scalapress.folder.tag

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.folder.{FolderDao, Folder}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import org.mockito.Mockito

/** @author Stephen Samuel */
class PrimaryFoldersTagTest extends FunSuite with MockitoSugar with BeforeAndAfter {

    val root = new Folder
    root.id = 1

    val folder1 = new Folder
    folder1.id = 123
    folder1.name = "Earl Grey"
    folder1.parent = root

    val folder2 = new Folder
    folder2.id = 667
    folder2.name = "Assam"
    folder2.parent = root

    root.subfolders.add(folder1)
    root.subfolders.add(folder2)

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext()
    context.folderDao = mock[FolderDao]
    val sreq = ScalapressRequest(req, context)
    Mockito.when(context.folderDao.root).thenReturn(root)

    test("rendering happy path") {

        val actual = PrimaryFoldersTag.render(sreq, Map.empty).get.replaceAll("\\s{2,}", "").replace("\n", "")
        assert(
            "<span class='cat_link'><a href='/folder-667-assam'>Assam</a></span><span class='cat_link'><a href='/folder-123-earl-grey'>Earl Grey</a></span>" === actual)
    }
}
