package com.cloudray.scalapress.plugin.url.simpleurls

import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.util.UrlStrategy

/** @author Stephen Samuel
  *
  *         Examples
  *
  *         www.countryshowguide.co.uk/f13-castles-in-wales
  *         www.countryshowguide.co.uk/i58-caerphilly-castle
  *
  **/
object SimpleUrlGeneratorStrategy extends UrlStrategy {

  def url(folder: Folder): String = "/f" + folder.id + "-" + normalize(folder.name)
  def url(item: Item): String = "/i" + item.id + "-" + normalize(item.name)
}

