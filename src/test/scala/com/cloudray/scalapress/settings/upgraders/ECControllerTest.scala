package com.cloudray.scalapress.settings.upgraders

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.obj.{Obj, ObjectDao}
import com.cloudray.scalapress.folder.{Folder, FolderDao}
import org.mockito.Mockito

/** @author Stephen Samuel */
class ECControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new ECRedirectController
    controller.context = new ScalapressContext
    controller.context.objectDao = mock[ObjectDao]
    controller.context.folderDao = mock[FolderDao]

    val obj = new Obj
    obj.id = 14
    obj.name = "big man tshirts"
    Mockito.when(controller.context.objectDao.find(14)).thenReturn(obj)

    val f = new Folder
    f.id = 28
    f.name = "big man tshirts"
    Mockito.when(controller.context.folderDao.find(28)).thenReturn(f)

    test("category.do redirects to folder page") {
        val url = controller.category(28)
        assert("redirect:/folder-28-big-man-tshirts" === url)
    }

    test("item.do redirects to object page") {
        val url = controller.item(14)
        assert("redirect:/object-14-big-man-tshirts" === url)
    }

    test("invalid category id redirects to home page") {
        val url = controller.category(645)
        assert("redirect:/" === url)
    }

    test("invalid item id redirects to home page") {
        val url = controller.item(97)
        assert("redirect:/" === url)
    }
}
