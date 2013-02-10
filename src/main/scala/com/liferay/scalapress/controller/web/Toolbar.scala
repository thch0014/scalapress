package com.liferay.scalapress.controller.web

import com.liferay.scalapress.domain.{Folder, Obj}
import com.liferay.scalapress.domain.setup.Site

/** @author Stephen Samuel */
object Toolbar {

    def render(site: Site, folder: Folder): String = {
        val url = "/backoffice/folder/" + folder.id
        render(site, url)
    }

    def render(site: Site, obj: Obj): String = {
        val url = "/backoffice/object/" + obj.id
        render(site, url)
    }

    def render(site: Site, url: String): String =
        <div class="navbar navbar-static-top navbar-inverse">
            <div class="navbar-inner">
                <div class="container" style="width: auto; padding: 0 20px;">
                    <a class="brand" href="#">
                        {site.name}
                    </a>
                    <ul class="nav">
                        <li class="active">
                            <a href="/backoffice/">Dashboard</a>
                        </li>
                        <li>
                            <a href="/">Home Page</a>
                        </li>
                        <li>
                            <a href={url}>Edit Page</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>.toString()
}
