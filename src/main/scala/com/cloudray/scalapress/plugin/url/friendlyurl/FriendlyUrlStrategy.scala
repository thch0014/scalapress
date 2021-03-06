package com.cloudray.scalapress.plugin.url.friendlyurl

import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.util.UrlStrategy

/**
 *
 * An implementation of url generator that provides single level friendly URLs in the format pagetype-id-pagename
 *
 * Eg,
 *
 * object-51-large-coldplay-tshirt
 *
 * @author Stephen Samuel
 **/
object FriendlyUrlStrategy extends UrlStrategy {
  def url(item: Item): String = "/item-" + item.id + "-" + normalize(item.name)
  def url(folder: Folder): String = "/folder-" + folder.id + "-" + normalize(Option(folder.name).getOrElse(""))
}
