package com.liferay.scalapress.service

import com.liferay.scalapress.domain.{Folder, Obj}

/** @author Stephen Samuel */
object FriendlyUrlGenerator {

    def friendlyLink(obj: Obj) = "<a href='" + friendlyUrl(obj) + "'>" + obj.name + "</a>"
    def friendlyLink(folder: Folder) = "<a href='" + friendlyUrl(folder) + "'>" + folder.name + "</a>"

    def friendlyUrl(obj: Obj): String = {
        "/object-" + obj.id + "-" + obj.name.replaceAll("[^a-zA-Z0-9\\s\\-]", "")
          .replaceAll("\\s", "-")
          .replaceAll("-{2,}", "-")
          .replaceAll("-$", "")
          .replaceAll("^-", "")
          .toLowerCase

    }

    def friendlyUrl(folder: Folder): String = {
        "/folder-" + folder.id + "-" + folder.name.replaceAll("[^a-zA-Z0-9\\s\\-]", "")
          .replaceAll("\\s", "-")
          .replaceAll("-{2,}", "-")
          .replaceAll("-$", "")
          .replaceAll("^-", "")
          .toLowerCase
    }
}
