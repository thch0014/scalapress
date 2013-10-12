package com.cloudray.scalapress.plugin.profile.controller.admin

import com.cloudray.scalapress.settings.{Menu, MenuItemProvider, MenuLink, MenuItem}
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class AccountPluginMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Option[MenuItem] =
    Some(Menu("Account", Some("icon-user"), Seq(
      MenuLink("Account Plugin", Some("icon-user"), "/backoffice/plugin/account"))))
}
