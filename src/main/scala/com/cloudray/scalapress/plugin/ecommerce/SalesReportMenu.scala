package com.cloudray.scalapress.plugin.ecommerce

import com.cloudray.scalapress.settings.lifecycle.MenuItem

/** @author Stephen Samuel */
class SalesReportMenu extends MenuItem {
    def link: Option[String] = Some("/backoffice/plugin/shopping/salesreport")
    def icon: Option[String] = Some("icon-list-alt")
    def name: String = "Sales Report"
}
