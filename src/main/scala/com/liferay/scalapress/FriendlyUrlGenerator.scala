package com.liferay.scalapress

import folder.Folder
import obj.Obj

/** @author Stephen Samuel */
object FriendlyUrlGenerator {

    def friendlyLink(obj: Obj): String = friendlyLink(obj, obj.name)
    def friendlyLink(obj: Obj, label: String): String =
        <a href={friendlyUrl(obj)}>
            {label}
        </a>.toString()

    def friendlyLink(folder: Folder): String = friendlyLink(folder, folder.name)
    def friendlyLink(folder: Folder,
                     label: String): String =
        <a href={friendlyUrl(folder)}>
            {label}
        </a>.toString()

    def friendlyUrl(id: Long, name: String) =
        "/object-" + id + "-" + name.replaceAll("[^a-zA-Z0-9\\s\\-]", "")
          .replaceAll("\\s", "-")
          .replaceAll("-{2,}", "-")
          .replaceAll("-$", "")
          .replaceAll("^-", "")
          .toLowerCase

    def friendlyUrl(obj: Obj): String = friendlyUrl(obj.id, obj.name)

    def friendlyUrl(folder: Folder): String = {
        "/folder-" + folder.id + "-" + Option(folder.name).getOrElse("").replaceAll("[^a-zA-Z0-9\\s\\-]", "")
          .replaceAll("\\s", "-")
          .replaceAll("-{2,}", "-")
          .replaceAll("-$", "")
          .replaceAll("^-", "")
          .toLowerCase
    }
}
