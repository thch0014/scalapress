package com.cloudray.scalapress.util

import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.plugin.friendlyurl.FriendlyUrlGenerator

/** @author Stephen Samuel */
case class Page(id: Long, name: String, url: String)

object Page {
    def apply(obj: Obj) = new Page(obj.id, obj.name, FriendlyUrlGenerator.url(obj))
    def apply(f: Folder) = new Page(f.id, f.name, FriendlyUrlGenerator.url(f))
}

