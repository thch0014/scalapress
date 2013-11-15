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
  def url(item: Item): String = strategy.url(item)

  // creates a link that uses the item name as the label
  def link(item: Item): String = link(item, item.name)
  // creates a link that uses the specified label
  def link(item: Item, label: String): String =
    Utility.trim(<a href={url(item)}>
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
  def url(item: Item): String = "/item/" + item.id
}
