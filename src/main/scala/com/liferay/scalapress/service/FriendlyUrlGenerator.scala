package com.liferay.scalapress.service

import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.folder.Folder

/** @author Stephen Samuel */
object FriendlyUrlGenerator {

    def friendlyLink(obj: Obj): String = friendlyLink(obj, obj.name)
    def friendlyLink(obj: Obj, label: String): String = "<a href=\"" + friendlyUrl(obj) + "\">" + label + "</a>"
    def friendlyLink(folder: Folder): String = friendlyLink(folder, folder.name)
    def friendlyLink(folder: Folder, label: String): String = "<a href=\"" + friendlyUrl(folder) + "\">" + label + "</a>"

    def friendlyUrl(obj: Obj): String = {
        "/object-" + obj.id + "-" + Option(obj.name).getOrElse("").replaceAll("[^a-zA-Z0-9\\s\\-]", "")
          .replaceAll("\\s", "-")
          .replaceAll("-{2,}", "-")
          .replaceAll("-$", "")
          .replaceAll("^-", "")
          .toLowerCase

    }

    def friendlyUrl(folder: Folder): String = {
        "/folder-" + folder.id + "-" + Option(folder.name).getOrElse("").replaceAll("[^a-zA-Z0-9\\s\\-]", "")
          .replaceAll("\\s", "-")
          .replaceAll("-{2,}", "-")
          .replaceAll("-$", "")
          .replaceAll("^-", "")
          .toLowerCase
    }
}
