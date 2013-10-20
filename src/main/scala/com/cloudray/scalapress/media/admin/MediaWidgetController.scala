package com.cloudray.scalapress.media.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, ModelAttribute, RequestMethod, RequestMapping}
import com.cloudray.scalapress.widgets.Widget
import org.springframework.web.multipart.MultipartFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.ModelMap
import java.net.URLConnection
import scala.collection.JavaConverters._
import com.cloudray.scalapress.media.{Asset, AssetStore, MediaWidget}
import com.cloudray.scalapress.widgets.controller.WidgetEditController

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/widget/media/{id}"))
@Autowired
class MediaWidgetController(assetStore: AssetStore) extends WidgetEditController {

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  override def edit(@ModelAttribute("widget") w: Widget, model: ModelMap) = {

    val assets = w.asInstanceOf[MediaWidget].sortedImages.map(img => {
      Asset(img, 0, assetStore.link(img),
        URLConnection.guessContentTypeFromName(img))
    })

    model.put("assets", assets.asJava)
    "admin/widget/media.vm"
  }

  @RequestMapping(value = Array("saveAndUpload"), method = Array(RequestMethod.POST), produces = Array("text/html"))
  def save(@ModelAttribute w: Widget, model: ModelMap,
           @RequestParam(value = "upload", required = false) upload: MultipartFile) = {

    if (upload != null && !upload.isEmpty) {
      val key = assetStore.add(upload.getOriginalFilename, upload.getInputStream)
      w.asInstanceOf[MediaWidget].images.clear()
      w.asInstanceOf[MediaWidget].images.add(key)
    }

    widgetDao.save(w)
    edit(w, model)
  }
}

