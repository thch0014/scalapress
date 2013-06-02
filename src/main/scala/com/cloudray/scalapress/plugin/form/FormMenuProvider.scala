package com.cloudray.scalapress.plugin.form

import com.cloudray.scalapress.settings.{MenuLink, Menu, MenuItem, MenuItemProvider}

/** @author Stephen Samuel */
class FormMenuProvider extends MenuItemProvider {
    def item: MenuItem =
        Menu("Forms", Some("icon-align-center"), Seq(
            MenuLink("Show Forms", Some("icon-align-center"), "/backoffice/form"),
            MenuLink("Submissions", Some("icon-pencil"), "/backoffice/submission")))
}
