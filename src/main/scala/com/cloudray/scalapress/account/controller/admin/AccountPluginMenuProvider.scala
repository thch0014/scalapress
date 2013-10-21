package com.cloudray.scalapress.account.controller.admin

import com.cloudray.scalapress.settings.{MenuItemProvider, MenuLink, MenuItem}
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class AccountPluginMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Seq[MenuItem] =
    Seq(MenuLink("Account Plugin", Some("glyphicon glyphicon-user"), "/backoffice/plugin/account"))
}
