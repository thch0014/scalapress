package com.cloudray.scalapress.account.controller.admin

import com.cloudray.scalapress.settings.{MenuHeader, MenuItemProvider, MenuLink, MenuItem}
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class AccountPluginMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Seq[MenuItem] =
    Seq(
      MenuHeader("Accounts"),
      MenuLink("Account Plugin", Some("glyphicon glyphicon-user"), "/backoffice/plugin/account")
    )
}
