package com.liferay.scalapress.controller.admin.media

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, RequestParam, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.service.asset.AssetStore
import com.liferay.scalapress.controller.admin.UrlResolver
import org.springframework.web.multipart.MultipartFile
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/medialib"))
class MediaLibraryController {

    @Autowired var assetStore: AssetStore = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
    def list = "admin/media/library.vm"

    @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
    def upload(@RequestParam("upload") uploads: java.util.List[MultipartFile]): String = {
        for (upload <- uploads.asScala)
            assetStore.put(upload.getOriginalFilename, upload.getInputStream)
        "redirect:" + UrlResolver.medialib
    }

    import scala.collection.JavaConverters._

    @ModelAttribute("assets") def assets(@RequestParam(value = "q", required = false) q: String) =
        assetStore.search(q, 50).toList.asJava
}
