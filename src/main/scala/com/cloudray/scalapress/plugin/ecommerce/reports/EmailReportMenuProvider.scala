package com.cloudray.scalapress.plugin.ecommerce.reports

import scala.Some
import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class EmailReportMenuProvider extends MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Shopping",
      "Email Report",
      Some("glyphicon glyphicon-envelope"),
      "/backoffice/plugin/ecommerce/report/email"))
  }
}
