package com.cloudray.scalapress.plugin.url.friendlyurl

import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.util.UrlStrategy

/** @author Stephen Samuel
  *
  *         An implementation of url generator that provides single level friendly URLs in the format pagetype-id-pagename
  *
  *         Eg,
  *
  *         object-51-large-coldplay-tshirt
  *
  * */
object FriendlyUrlStrategy extends UrlStrategy {
  def url(obj: Obj): String = "/object-" + obj.id + "-" + normalize(obj.name)
  def url(folder: Folder): String = "/folder-" + folder.id + "-" + normalize(Option(folder.name).getOrElse(""))
}
