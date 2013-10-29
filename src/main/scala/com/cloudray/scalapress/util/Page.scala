package com.cloudray.scalapress.util

import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
case class Page(id: Long, name: String, url: String)

object Page {
  def apply(obj: Item) = new Page(obj.id, obj.name, UrlGenerator.url(obj))
  def apply(f: Folder) = new Page(f.id, f.name, UrlGenerator.url(f))
}

