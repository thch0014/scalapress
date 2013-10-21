package com.cloudray.scalapress.account.controller.admin

import com.cloudray.scalapress.settings.{Menu, MenuItemProvider, MenuLink, MenuItem}
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
class AccountPluginMenuProvider extends MenuItemProvider {

  def item(context: ScalapressContext): Option[MenuItem] =
    Some(Menu("Account", Some("glyphicon glyphicon-user"), Seq(
      MenuLink("Account Plugin", Some("glyphicon glyphicon-user"), "/backoffice/plugin/account"))))
}
