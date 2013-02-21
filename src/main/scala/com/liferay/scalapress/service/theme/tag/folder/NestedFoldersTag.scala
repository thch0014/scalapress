package com.liferay.scalapress.service.theme.tag.folder

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import collection.mutable.ArrayBuffer
import com.liferay.scalapress.domain.Folder
import scala.collection.JavaConverters._
import com.liferay.scalapress.service.FriendlyUrlGenerator

/** @author Stephen Samuel */
object NestedFoldersTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        val depth = params.get("depth").getOrElse("1").toInt
        val excluded = params.get("exclude").getOrElse("").split(",")
        val root = request.context.folderDao.root
        val buffer = new ArrayBuffer[String]
        buffer.append("<div class=\"nested-folders\">")
        _renderLevel(root, depth - 1, buffer, excluded)
        buffer.append("</div>")
        val string = buffer.mkString("\n")
        Some(string)
    }

    private def _renderLevel(root: Folder, depth: Int, buffer: ArrayBuffer[String], excluded: Array[String]) {
        buffer.append("<ul class=\"nested-folder-level" + depth + "\">")
        root.subfolders.asScala.filterNot(f => excluded.contains(f.id.toString)).foreach(child => {
            buffer
              .append("<li id=\"nested-folder-" + child.id + "\"><a href=\"" + FriendlyUrlGenerator
              .friendlyLink(child) + "\">" + child.name + "</a>")
            if (depth > 0)
                _renderLevel(child, depth - 1, buffer, excluded)
            buffer.append("</li>")
        })
        buffer.append("</ul>")
    }
}
