package com.cloudray.scalapress.util.mvc

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.OrderDao
import org.mockito.Mockito
import com.cloudray.scalapress.item.ItemDao
import com.cloudray.scalapress.folder.FolderDao
import com.cloudray.scalapress.search.SearchService

/** @author Stephen Samuel */
class DashboardControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val context = new ScalapressContext
  context.itemDao = mock[ItemDao]
  context.orderDao = mock[OrderDao]
  context.folderDao = mock[FolderDao]
  context.searchService = mock[SearchService]
  val controller = new DashboardController(context)

  "a dashboard controller" should "include 8 recent items" in {
    controller.recentObjects
    Mockito.verify(context.itemDao).recent(8)
  }

  it should "include folders count" in {
    controller.folderCount
    Mockito.verify(context.folderDao).count
  }

  it should "include indexed item count" in {
    controller.indexed
    Mockito.verify(context.searchService).count
  }
}
