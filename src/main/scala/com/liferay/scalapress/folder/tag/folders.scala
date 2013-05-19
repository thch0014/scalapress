package com.liferay.scalapress.folder.tag

import com.liferay.scalapress.ScalapressRequest
import collection.mutable.ArrayBuffer
import com.liferay.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import com.liferay.scalapress.plugin.friendlyurl.FriendlyUrlGenerator

/** @author Stephen Samuel */

// link to a specific folder or the one in the current context
object FolderTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = {

        val folder = params
          .get("id")
          .flatMap(id => Option(request.context.folderDao.find(id.toLong)))
          .orElse(request.folder)

        folder.map(f => {
            val text = params.get("text").getOrElse(f.name)
            params.contains("link") match {
                case true => FriendlyUrlGenerator.friendlyLink(f, text)
                case false => text
            }
        })
    }
}

object SubfoldersTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = {
        request.folder.map(_.sortedSubfolders.map(FriendlyUrlGenerator.friendlyLink(_)).mkString("\n"))
    }
}

object BreadcrumbsTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

        val sep = params.get("sep").getOrElse("/")
        val excludeHome = params.contains("exhome")
        val home = request.folder.exists(_.parent == null)
        if (excludeHome && home)
            None
        else
            request.folder.map(folder => {

                val cssClass = params.get("class").getOrElse("breadcrumb")
                val tag = params.get("tag").getOrElse("ul")

                val buffer = new ArrayBuffer[String]

                var parent = folder.parent
                while (parent != null) {
                    buffer.prepend("<li>" + FriendlyUrlGenerator
                      .friendlyLink(parent) + " <span class='divider'>" + sep + "</span></li>")
                    parent = parent.parent
                }
                buffer.append("<li class='active'>" + folder.name + "</li>")

                buffer.prepend("<" + tag + " class='" + cssClass + "'>")
                buffer.append("</" + tag + ">")

                buffer.mkString("\n")
            })
    }
}

// shows top level folders
object PrimaryFoldersTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = {

        val tag = params.get("tag").getOrElse("span")
        val cssClass = params.get("class").getOrElse("cat_link")
        val exclude = params.get("exclude").getOrElse("").split(",")
        val sep = params.get("sep").getOrElse("")
        val activeClass = params.get("activeclass").getOrElse("current active")

        val root = request.context.folderDao.root
        val filtered = root.sortedSubfolders.filterNot(_.hidden).filterNot(f => exclude.contains(f.id.toString))

        val links = filtered.map(f => {

            val startTag = if (request.folder.orNull == f)
                "<" + tag + " class='" + cssClass + " " + activeClass + "'>"
            else
                "<" + tag + " class='" + cssClass + "'>"

            val endTag = "</" + tag + ">"

            val link = "<a href='" + FriendlyUrlGenerator.friendlyUrl(f) + "'>" + f.name + "</a>"
            startTag + link + endTag

        }).mkString(sep)

        Some(links)
    }
}