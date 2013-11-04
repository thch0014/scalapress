package com.cloudray.scalapress.media.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile
import scala.collection.JavaConverters._
import com.cloudray.scalapress.media.{AssetQuery, AssetService, AssetStore}
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.ModelMap
import com.sksamuel.scoot.soa.{Paging, Page}
import com.cloudray.scalapress.search.PagingRenderer
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/medialib"))
class MediaLibraryController(assetStore: AssetStore,
                             assetService: AssetService,
                             context: ScalapressContext) {

  val PAGE_SIZE = 36

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
  def list = "admin/media/library.vm"

  @RequestMapping(value = Array("delete/{key}"), produces = Array("text/html"))
  def delete(@PathVariable("key") key: String): String = {
    assetStore.delete(key)
    "redirect:/backoffice/medialib"
  }

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
  def upload(@RequestParam("upload") uploads: java.util.List[MultipartFile]): String = {
    assetService.upload(uploads.asScala.filter(_ != null))
    "redirect:/backoffice/medialib"
  }

  @ModelAttribute def assets(@RequestParam(value = "q", required = false) prefix: String,
                             @RequestParam(value = "pageNumber", required = false, defaultValue = "1") pageNumber: Int,
                             req: HttpServletRequest,
                             model: ModelMap) {

    val assets = assetStore.search(AssetQuery(prefix, pageNumber, PAGE_SIZE))
    model.put("assets", assets)

    val page = Page(assets, pageNumber, PAGE_SIZE, assetStore.count)
    val paging = Paging(req, page)
    model.put("paging", paging)

    val pagination = PagingRenderer.render(paging, 10)
    model.put("pagination", pagination)
  }
}
