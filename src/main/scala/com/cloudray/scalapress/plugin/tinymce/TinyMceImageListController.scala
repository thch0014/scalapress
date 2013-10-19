package com.cloudray.scalapress.plugin.tinymce

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.media.{AssetQuery, AssetStore}
import org.apache.commons.io.FilenameUtils

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("/backoffice/tinymce"))
class TinyMceImageListController {

  val extensions = List("jpg", "jpeg", "png", "gif")

  @Autowired var assetStore: AssetStore = _

  @ResponseBody
  @RequestMapping(value = Array("image"), produces = Array("text/javascript"))
  def images: String = {
    val sb = new StringBuffer("var tinyMCEImageList = new Array(")

    val assets = getAssets
    val array = assets
      .map(asset => "[\"" + asset.filename.replace("\"", "\\\"") + "\",\"" + asset.url.replace("\"", "\\\"") + "\"]")
    sb.append(array.mkString(", "))

    sb.append(");")
    sb.toString
  }

  def getAssets = assetStore.search(AssetQuery(1, 10000))
    .filterNot(_.filename.startsWith("_thumbnail"))
    .filter(arg => extensions.contains(FilenameUtils.getExtension(arg.filename)))
}
