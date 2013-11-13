package com.cloudray.scalapress.plugin.ecommerce.reports

import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class SalesReportMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): (String, Seq[MenuItem]) = {
    ("Shopping",
      Seq(
        MenuItem("Sales Report", Some("glyphicon glyphicon-list-alt"), "/backoffice/plugin/shopping/salesreport")
      ))
  }
}
