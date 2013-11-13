package com.cloudray.scalapress.util.mvc

import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters._
import com.cloudray.scalapress.security.SpringSecurityResolver
import javax.servlet.http.HttpServletRequest
import java.util.Properties
import org.apache.commons.io.IOUtils
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import com.googlecode.genericdao.search.Search
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.{OrderTotal, OrderDao}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice"))
class DashboardController {

  val in = getClass.getResourceAsStream("/buildNumber.properties")
  val props = new Properties()
  props.load(in)
  IOUtils.closeQuietly(in)

  @Autowired var context: ScalapressContext = _
  @Autowired var orderDao: OrderDao = _

  @RequestMapping(produces = Array("text/html"))
  def dashboard = "admin/dashboard.vm"

  @ModelAttribute("orderTotals")
  def orderTotals: java.util.List[OrderTotal] = {
    val orders = orderDao.ordersPerDay(180)
    orders.asJava
  }

  @ModelAttribute("installation")
  def installation = context.installationDao.get

  @ModelAttribute("indexed")
  def indexed = context.searchService.count

  @ModelAttribute("user")
  def indexed(req: HttpServletRequest) = SpringSecurityResolver.getAdminDetails(req).user

  @ModelAttribute("folderCount")
  def folderCount = context.folderDao.count(new Search(classOf[Folder]))

  @ModelAttribute("orderCount")
  def orderCount = context.orderDao.count(new Search(classOf[Order]))

  @ModelAttribute("recentObjects")
  def recentObjects: java.util.List[Item] = context.itemDao.recent(8).asJava

  @ModelAttribute("buildNumber")
  def buildNumber = props.get("buildNumber")
}
