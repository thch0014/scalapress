package com.cloudray.scalapress.plugin.compatibility

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.{Item, ItemDao}
import com.cloudray.scalapress.folder.{Folder, FolderDao}
import org.mockito.Mockito
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
class ECRedirectControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val context = new ScalapressContext
  context.itemDao = mock[ItemDao]
  context.folderDao = mock[FolderDao]
  val controller = new ECRedirectController(context)

  val obj = new Item
  obj.id = 14
  obj.name = "big man tshirts"
  Mockito.when(context.itemDao.find(14)).thenReturn(obj)

  val f = new Folder
  f.id = 28
  f.name = "big man tshirts"
  Mockito.when(context.folderDao.find(28)).thenReturn(f)

  test("category.do redirects to folder page") {
    val url = controller.category(28)
    assert("redirect:/folder-28-big-man-tshirts" === url)
  }

  test("item.do redirects to item page") {
    val url = controller.item(14)
    assert("redirect:/item-14-big-man-tshirts" === url)
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
