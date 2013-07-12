package com.cloudray.scalapress.util.mvc

import com.cloudray.scalapress.ScalapressRequest

/** @author Stephen Samuel */
class Toolbar(name: String, url: Option[String]) {

  def render: String = {

    val editListItem = url.map(u => {
      <li>
        <a href={u}>Edit This Page</a>
      </li>
    })

    <div class="navbar navbar-static-top navbar-inverse">
      <div class="navbar-inner">
        <div class="container" style="width: auto; padding: 0 20px;">
          <a class="brand" href="#">
            {name}
          </a>
          <ul class="nav">
            <li>
              <a href="/backoffice/">Dashboard</a>
            </li>
            <li>
              <a href="/">Home Page</a>
            </li>{editListItem.orNull}
          </ul>
        </div>
      </div>
    </div>.toString()
  }
}

object Toolbar {

  def apply(sreq: ScalapressRequest) = {
    val folderUrl = sreq.folder.map(f => "/backoffice/folder/" + f.id)
    val objectUrl = sreq.obj.map(o => "/backoffice/object/" + o.id)
    val url = folderUrl.orElse(objectUrl)
    new Toolbar(sreq.installation.name, url)
  }
}
