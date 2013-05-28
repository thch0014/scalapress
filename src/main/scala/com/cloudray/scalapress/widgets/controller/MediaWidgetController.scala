package com.cloudray.scalapress.widgets.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import com.cloudray.scalapress.widgets.Widget
import org.springframework.web.multipart.MultipartFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.ModelMap
import java.net.URLConnection
import scala.collection.JavaConverters._
import com.cloudray.scalapress.media.{Asset, AssetStore, MediaWidget, Image}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/widget/media/{id}"))
class MediaWidgetController extends WidgetEditController {

    @Autowired var assetStore: AssetStore = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    override def edit(@ModelAttribute("widget") w: Widget, model: ModelMap) = {

        val assets = w.asInstanceOf[MediaWidget].images.asScala.map(img => {
            Asset(img.filename, 0, assetStore.link(img.filename),
                URLConnection.guessContentTypeFromName(img.filename))
        })

        model.put("assets", assets.asJava)
        "admin/widget/media.vm"
    }

    @RequestMapping(value = Array("saveAndUpload"), method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute w: Widget, model: ModelMap,
             @RequestParam(value = "upload", required = false) upload: MultipartFile) = {

        if (upload != null && !upload.isEmpty) {
            val key = assetStore.add(upload.getOriginalFilename, upload.getInputStream)
            val image = Image(key)
            image.mediaWidget = w.asInstanceOf[MediaWidget]
            w.asInstanceOf[MediaWidget].images.clear()
            w.asInstanceOf[MediaWidget].images.add(image)
        }

        widgetDao.save(w)
        edit(w, model)
    }
}

