package com.liferay.scalapress.folder.tag

import com.liferay.scalapress.{Tag, ScalapressContext, ScalapressRequest}
import collection.mutable.ArrayBuffer
import scala.collection.JavaConverters._
import com.liferay.scalapress.folder.Folder
import com.liferay.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import com.liferay.scalapress.plugin.friendlyurl.FriendlyUrlGenerator

/** @author Stephen Samuel */
@Tag("folders_nested")
class NestedFoldersTag extends ScalapressTag with TagBuilder {

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

    private def _renderLevel(parent: Folder, depth: Int, buffer: ArrayBuffer[String], excluded: Array[String]) {

        val folders = parent.subfolders.asScala.toSeq
        val filtered = folders.filterNot(f => excluded.contains(f.id.toString)).filterNot(_.hidden)
        val sorted = filtered.sortBy(_.name)
        buffer.append("<ul class=\"nested-folder-level" + depth + "\">")
        sorted.foreach(child => {
            buffer.append("<li id=\"nested-folder-" + child.id + "\">")
            buffer.append(FriendlyUrlGenerator.friendlyLink(child))
            if (depth > 0)
                _renderLevel(child, depth - 1, buffer, excluded)
            buffer.append("</li>")
        })
        buffer.append("</ul>")
    }
}
