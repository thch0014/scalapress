package com.cloudray.scalapress.account.controller.admin

import com.cloudray.scalapress.framework.{ScalapressContext, MenuProvider, MenuItem}

/** @author Stephen Samuel */
class AccountPluginMenuProvider extends MenuProvider {

  def menu(context: ScalapressContext): (String, Seq[MenuItem]) =
    ("Accounts",
      Seq(
        MenuItem("Account Plugin", Some("glyphicon glyphicon-user"), "/backoffice/plugin/account")
      ))
}
