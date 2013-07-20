package com.cloudray.scalapress.util

import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.search.ObjectRef
import com.cloudray.scalapress.plugin.url.friendlyurl.FriendlyUrlStrategy
import scala.xml.Utility

/** @author Stephen Samuel */
object UrlGenerator {

  private[util] var strategy: UrlStrategy = FriendlyUrlStrategy

  def url(ref: ObjectRef): String = {
    val o = new Obj
    o.id = ref.id
    o.name = ref.name
    url(o)
  }

  def url(folder: Folder): String = strategy.url(folder)
  def url(obj: Obj): String = strategy.url(obj)

  // creates a link that uses the object name as the label
  def link(obj: Obj): String = link(obj, obj.name)
  // creates a link that uses the specified label
  def link(obj: Obj, label: String): String =
    Utility.trim(<a href={url(obj)}>
      {label}
    </a>).toString()

  // creates a link that uses the folder name as the label
  def link(folder: Folder): String = link(folder, folder.name)
  // creates a link that uses the specified label
  def link(folder: Folder, label: String): String =
    Utility.trim(<a href={url(folder)}>
      {label}
    </a>).toString()
}

object DefaultUrlStrategy extends UrlStrategy {
  def url(folder: Folder): String = "/folder/" + folder.id
  def url(obj: Obj): String = "/object/" + obj.id
}
