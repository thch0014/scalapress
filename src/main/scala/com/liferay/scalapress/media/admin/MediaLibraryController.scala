package com.liferay.scalapress.media.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, RequestParam, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import org.springframework.web.multipart.MultipartFile
import scala.collection.JavaConverters._
import com.liferay.scalapress.util.mvc.UrlResolver
import com.liferay.scalapress.media.{AssetLifecycleListener, AssetStore}
import java.io._
import org.springframework.context.ApplicationContext
import org.springframework.ui.ModelMap
import com.sksamuel.scoot.soa.{Paging, Page}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.search.PagingRenderer

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/medialib"))
class MediaLibraryController {

    val PAGE_SIZE = 36

    @Autowired var assetStore: AssetStore = _
    @Autowired var context: ScalapressContext = _
    @Autowired var appContext: ApplicationContext = _

    @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
    def list = "admin/media/library.vm"

    @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
    def upload(@RequestParam(value = "minify", defaultValue = "false") minify: Boolean,
               @RequestParam("upload") uploads: java.util.List[MultipartFile]): String = {

        val listeners = appContext.getBeansOfType(classOf[AssetLifecycleListener]).values().asScala
        for ( upload <- uploads.asScala.filter(_ != null).filter(!_.isEmpty) ) {
            val key = upload.getOriginalFilename
            val in = upload.getInputStream
            val start = (key, in)
            val op = (a: (String, InputStream), b: AssetLifecycleListener) => b.onStore(a._1, a._2)
            val result = listeners.foldLeft(start)(op)
            assetStore.put(result._1, result._2)
        }
        "redirect:" + UrlResolver.medialib
    }

    @ModelAttribute def assets(@RequestParam(value = "q", required = false) q: String,
                               @RequestParam(value = "pageNumber", required = false, defaultValue = "1") pageNumber: Int,
                               req: HttpServletRequest,
                               model: ModelMap) {

        val assets = assetStore.search(q, pageNumber, PAGE_SIZE).toList
        model.put("assets", assets.asJava)

        val page = Page(assets, pageNumber, PAGE_SIZE, assetStore.count)
        val paging = Paging(req, page)
        model.put("paging", paging)

        val pagination = PagingRenderer.render(paging, 10)
        model.put("pagination", pagination)
    }
}
