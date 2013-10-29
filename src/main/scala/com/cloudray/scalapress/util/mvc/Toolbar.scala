package com.cloudray.scalapress.util.mvc

import com.cloudray.scalapress.ScalapressRequest
import com.cloudray.scalapress.util.Scalate

/** @author Stephen Samuel */
class Toolbar(name: String, url: Option[String]) {

  def render: String = {

    val edit = url.map(u => s"<li><a href=$u>Edit This Page</a></li>").getOrElse("")

    Scalate.layout("/com/cloudray/scalapress/util/mvc/toolbar.ssp",
      Map("name" -> name, "edit" -> edit))
  }
}

object Toolbar {

  def apply(sreq: ScalapressRequest) = {
    val folderUrl = sreq.folder.map(f => "/backoffice/folder/" + f.id)
    val objectUrl = sreq.item.map(o => "/backoffice/object/" + o.id)
    val url = folderUrl.orElse(objectUrl)
    new Toolbar(sreq.installation.name, url)
  }
}
