package com.cloudray.scalapress.plugin.form

import com.cloudray.scalapress.settings.{MenuLink, Menu, MenuItem, MenuItemProvider}

/** @author Stephen Samuel */
class FormMenuProvider extends MenuItemProvider {
    def items: Seq[MenuItem] = Seq(
        Menu("Forms", Some("icon-align-center"), Seq(
            MenuLink("List Forms", Some("icon-align-center"), "/backoffice/form"),
            MenuLink("Submission", Some("icon-pencil"), "/backoffice/submission"))))
}
