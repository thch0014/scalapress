package com.cloudray.scalapress.plugin.friendlyurl

import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.util.UrlGenerator

/** @author Stephen Samuel
  *
  *         An implementation of url generator that provides single level friendly URLs in the format pagetype-id-pagename
  *
  *         Eg,
  *
  *         object-51-large-coldplay-tshirt
  *
  * */
object FriendlyUrlGenerator extends UrlGenerator {

    def url(obj: Obj): String = friendlyUrl(obj)
    def url(folder: Folder): String = friendlyUrl(folder)

    @deprecated
    def friendlyUrl(id: Long, name: String) = "/object-" + id + "-" + normalize(name)

    @deprecated
    def friendlyUrl(obj: Obj): String = friendlyUrl(obj.id, obj.name)

    @deprecated
    def friendlyUrl(folder: Folder): String = {
        "/folder-" + folder.id + "-" + normalize(Option(folder.name).getOrElse(""))
    }
}
