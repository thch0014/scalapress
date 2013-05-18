package com.liferay.scalapress.util

import com.liferay.scalapress.plugin.friendlyurl.FriendlyUrlGenerator
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.folder.Folder

/** @author Stephen Samuel */
case class Page(id: Long, name: String) {
    def url = FriendlyUrlGenerator.friendlyUrl(id, name)
}

object Page {
    def apply(obj: Obj) = new Page(obj.id, obj.name)
    def apply(f: Folder) = new Page(f.id, f.name)
}

