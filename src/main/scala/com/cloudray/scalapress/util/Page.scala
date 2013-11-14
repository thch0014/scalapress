package com.cloudray.scalapress.util

import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.framework.UrlGenerator

/** @author Stephen Samuel */
case class WebPage(id: Long, name: String, url: String)

object WebPage {
  def apply(obj: Item) = new WebPage(obj.id, obj.name, UrlGenerator.url(obj))
  def apply(f: Folder) = new WebPage(f.id, f.name, UrlGenerator.url(f))
}

