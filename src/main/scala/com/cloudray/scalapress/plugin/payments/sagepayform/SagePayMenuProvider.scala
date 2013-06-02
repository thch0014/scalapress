package com.cloudray.scalapress.plugin.payments.sagepayform

import com.cloudray.scalapress.settings.{MenuLink, MenuItem, MenuItemProvider}

/** @author Stephen Samuel */
class SagePayMenuProvider extends MenuItemProvider {
    def items: Seq[MenuItem] =
        Seq(MenuLink("Sage Pay", Some("icon-credit-card"), "/backoffice/plugin/sagepayform"))
}
