package com.cloudray.scalapress.plugin.tinymce

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.media.AssetStore

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("/backoffice/tinymce"))
class TinymceController {

    @Autowired var assetStore: AssetStore = _

    @ResponseBody
    @RequestMapping(value = Array("image"), produces = Array("text/javascript"))
    def imagelist: String = {
        val sb = new StringBuffer("var tinyMCEImageList = new Array(")

        val images = assetStore
          .list(10000)
          .filter(asset => asset.filename.endsWith("jpg") ||
          asset.filename.endsWith("jpeg") ||
          asset.filename.endsWith("png") ||
          asset.filename.endsWith("gif"))

        sb
          .append(images
          .map(img => "[\"" + img.filename.replace("\"", "\\\"") + "\",\"" + img.url.replace("\"", "\\\"") + "\"]")
          .mkString(", "))

        sb.append(");")
        sb.toString
    }
}