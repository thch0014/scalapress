package com.cloudray.scalapress.util.mvc

import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import scala.collection.JavaConverters._
import com.cloudray.scalapress.security.SpringSecurityResolver
import javax.servlet.http.HttpServletRequest
import java.util.Properties
import org.apache.commons.io.IOUtils
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice"))
class DashboardController {

  val in = getClass.getResourceAsStream("/buildNumber.properties")
  val props = new Properties()
  props.load(in)
  IOUtils.closeQuietly(in)

  @Autowired var context: ScalapressContext = _

  @RequestMapping(produces = Array("text/html"))
  def dashboard = "admin/dashboard.vm"

  //  @ModelAttribute("dailySales") def dailySales: java.util.Map[String, Int] = {
  //    val q = new OrderQuery
  //    q.pageSize = 100000000
  //    q.from = Some(new DateMidnight(DateTimeZone.UTC).minusDays(7).getMillis)
  //    q.to = Some(new DateMidnight(DateTimeZone.UTC).plusDays(1).getMillis)
  //    val orders = context.bean[OrderDao].search(q).results
  //    val grouped = orders.groupBy(order => new DateMidnight(order.datePlaced, DateTimeZone.UTC))
  //    val totals = grouped
  //      .map(group => (group._1.toString("dd/MM/yyyy"), group._2.foldLeft(0)((a, b) => a + b.total.toInt)))
  //    totals.asJava
  //  }

  @ModelAttribute("installation") def installation = context.installationDao.get
  @ModelAttribute("indexed") def indexed = context.searchService.count
  @ModelAttribute("user") def indexed(req: HttpServletRequest) = SpringSecurityResolver.getAdminDetails(req).user
  @ModelAttribute("folderCount") def folderCount = context.folderDao.findAll().size

  @ModelAttribute("recentObjects") def recentObjects: java.util.List[Obj] = context.objectDao.recent(8).asJava

  @ModelAttribute("buildNumber") def buildNumber = props.get("buildNumber")
}
