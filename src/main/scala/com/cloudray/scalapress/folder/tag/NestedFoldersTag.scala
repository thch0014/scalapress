package com.cloudray.scalapress.folder.tag

import collection.mutable.ArrayBuffer
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import com.cloudray.scalapress.util.UrlGenerator
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("folders_nested")
class NestedFoldersTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    val depth = params.get("depth").getOrElse("1").toInt
    val excluded = params.get("exclude").getOrElse("").split(",")
    val root = request.folderRoot
    val buffer = new ArrayBuffer[String]
    buffer.append("<div class=\"nested-folders\">")
    _renderLevel(root, depth - 1, buffer, excluded)
    buffer.append("</div>")
    val string = buffer.mkString("\n")
    Some(string)
  }

  private def _renderLevel(parent: Folder, depth: Int, buffer: ArrayBuffer[String], excluded: Array[String]) {

    val folders = parent.sortedSubfolders
    val filtered = folders.filterNot(f => excluded.contains(f.id.toString)).filterNot(_.hidden)
    val sorted = filtered.sortBy(_.name)
    buffer.append("<ul class=\"nested-folder-level" + depth + "\">")
    sorted.foreach(child => {
      buffer.append("<li id=\"nested-folder-" + child.id + "\">")
      buffer.append(UrlGenerator.link(child))
      if (depth > 0)
        _renderLevel(child, depth - 1, buffer, excluded)
      buffer.append("</li>")
    })
    buffer.append("</ul>")
  }
}
