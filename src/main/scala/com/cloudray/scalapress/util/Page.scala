package com.cloudray.scalapress.util

import com.cloudray.scalapress.plugin.friendlyurl.FriendlyUrlGenerator
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
case class Page(id: Long, name: String) {
    def url = FriendlyUrlGenerator.friendlyUrl(id, name)
}

object Page {
    def apply(obj: Obj) = new Page(obj.id, obj.name)
    def apply(f: Folder) = new Page(f.id, f.name)
}

