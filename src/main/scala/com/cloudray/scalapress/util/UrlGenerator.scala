package com.cloudray.scalapress.util

import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
trait UrlGenerator {

    def url(obj: Obj): String
    def url(folder: Folder): String

    // creates a link that uses the object name as the label
    def link(obj: Obj): String = friendlyLink(obj)
    @deprecated
    def friendlyLink(obj: Obj): String = friendlyLink(obj, obj.name)
    // creates a link that uses the specified label
    def link(obj: Obj, label: String): String = friendlyLink(obj, label)
    @deprecated
    def friendlyLink(obj: Obj, label: String): String =
        <a href={url(obj)}>
            {label}
        </a>.toString()

    // creates a link that uses the folder name as the label
    def link(folder: Folder): String = friendlyLink(folder)
    @deprecated
    def friendlyLink(folder: Folder): String = friendlyLink(folder, folder.name)

    // creates a link that uses the specified label
    def link(folder: Folder, label: String): String = friendlyLink(folder, label)
    @deprecated
    def friendlyLink(folder: Folder, label: String): String =
        <a href={url(folder)}>
            {label}
        </a>.toString()
}
