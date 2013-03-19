package com.liferay.scalapress.util.mvc

import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.folder.Folder
import com.liferay.scalapress.settings.Installation

/** @author Stephen Samuel */
object Toolbar {

    def render(site: Installation, folder: Folder): String = {
        val url = "/backoffice/folder/" + folder.id
        render(site, url)
    }

    def render(site: Installation, obj: Obj): String = {
        val url = "/backoffice/object/" + obj.id
        render(site, url)
    }

    def render(site: Installation, url: String): String =
        <div class="navbar navbar-static-top navbar-inverse">
            <div class="navbar-inner">
                <div class="container" style="width: auto; padding: 0 20px;">
                    <a class="brand" href="#">
                        {site.name}
                    </a>
                    <ul class="nav">
                        <li>
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
