package com.cloudray.scalapress.account.controller.admin

import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class AccountPluginMenuProvider extends MenuProvider {
  def menu(context: ScalapressContext): Option[MenuItem] = {
    Some(MenuItem("Accounts", "Account Plugin", Some("glyphicon glyphicon-user"), "/backoffice/plugin/account"))
  }
}
