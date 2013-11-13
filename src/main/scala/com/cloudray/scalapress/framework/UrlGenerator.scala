package com.cloudray.scalapress.framework

import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.search.ItemRef
import com.cloudray.scalapress.plugin.url.friendlyurl.FriendlyUrlStrategy
import scala.xml.Utility
import com.cloudray.scalapress.util.UrlStrategy

/** @author Stephen Samuel */
object UrlGenerator {

  private[framework] var strategy: UrlStrategy = FriendlyUrlStrategy

  def url(ref: ItemRef): String = {
    val o = new Item
    o.id = ref.id
    o.name = ref.name
    url(o)
  }

  def url(folder: Folder): String = strategy.url(folder)
  def url(obj: Item): String = strategy.url(obj)

  // creates a link that uses the object name as the label
  def link(obj: Item): String = link(obj, obj.name)
  // creates a link that uses the specified label
  def link(obj: Item, label: String): String =
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
  def url(obj: Item): String = "/item/" + obj.id
}
