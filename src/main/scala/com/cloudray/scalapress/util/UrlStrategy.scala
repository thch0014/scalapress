package com.cloudray.scalapress.util

import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
trait UrlStrategy {

    def url(obj: Obj): String
    def url(folder: Folder): String

    def normalize(label: String) =
        label
          .replaceAll("[^a-zA-Z0-9\\s\\-]", "")
          .replaceAll("\\s", "-")
          .replaceAll("-{2,}", "-")
          .replaceAll("-$", "")
          .replaceAll("^-", "")
          .toLowerCase
}
