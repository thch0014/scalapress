package com.liferay.scalapress.service.theme.tag.folder

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import collection.mutable.ArrayBuffer
import scala.collection.JavaConverters._
import com.liferay.scalapress.service.FriendlyUrlGenerator

/** @author Stephen Samuel */
object FolderTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        request.folder.map(f => {
            params.contains("link") match {
                case true => FriendlyUrlGenerator.friendlyLink(f)
                case false => f.name
            }
        })
    }
}

object SubfoldersTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        request.folder.map(f => {
            f.subfolders
              .asScala
              .map(FriendlyUrlGenerator.friendlyLink(_)).mkString("\n")
        })
    }
}

object BreadcrumbsTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

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
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {

        val tag = params.get("tag").getOrElse("span")
        val cssClass = params.get("class").getOrElse("cat_link")
        val exclude = params.get("exclude").getOrElse("").split(",")

        val startTag = "<" + tag + " class='" + cssClass + "'>"
        val endTag = "</" + tag + ">"

        val folders = context.folderDao.findTopLevel.filterNot(_.hidden).filterNot(f => exclude.contains(f.id))
        val names = folders
          .map(f => startTag + "<a href='" + FriendlyUrlGenerator.friendlyUrl(f) + "' class='current'>" + f
          .name + "</a>" + endTag)
          .mkString("\n")
        Some(names)
    }
}