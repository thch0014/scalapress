package com.cloudray.scalapress.item.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.sksamuel.scoot.soa.Paging
import org.springframework.ui.ModelMap
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item._
import scala.beans.BeanProperty
import org.springframework.web.multipart.MultipartFile
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.folder.FolderDao

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/item"))
class ItemSearchController(itemDao: ItemDao,
                           typeDao: ItemTypeDao,
                           folderDao: FolderDao,
                           context: ScalapressContext) extends ItemStatusPopulator {

  @RequestMapping
  def search(@ModelAttribute("form") form: SearchForm,
             @RequestParam(value = "typeId") typeId: Long,
             @RequestParam(value = "pageNumber", defaultValue = "1") pageNumber: Int,
             model: ModelMap,
             req: HttpServletRequest) = {

    val query = new ItemQuery
    query.pageNumber = pageNumber
    query.pageSize = 20
    query.status = Option(form.status)
    query.name = Option(form.name)
    query.typeId = Option(typeId).filter(_ > 0)

    val page = itemDao.search(query)
    model.put("objects", page.java)
    model.put("paging", Paging(req, page))

    "admin/item/list.vm"
  }

  @RequestMapping(value = Array("create"), params = Array("typeId"))
  def create(@RequestParam("typeId") typeId: java.lang.Long): String = {

    val t = typeDao.find(typeId)
    val obj = Item(t)
    itemDao.save(obj)

    "redirect:/backoffice/item/" + obj.id
  }

  @RequestMapping(value = Array("create"), params = Array("typeId", "name"))
  def create(@RequestParam("typeId") typeId: java.lang.Long, @RequestParam("name") name: String): String = {

    val t = typeDao.find(typeId)
    val obj = Item(t)
    obj.name = name
    itemDao.save(obj)

    "redirect:/backoffice/item/" + obj.id
  }

  @RequestMapping(value = Array("import"), params = Array("typeId"))
  def imp(@RequestParam("typeId") typeId: java.lang.Long,
          @RequestParam("upload") upload: MultipartFile): String = {

    val t = typeDao.find(typeId)
    val importer = new ItemImporter(itemDao, folderDao, t)
    importer.doImport(upload.getInputStream)

    "redirect:/backoffice/item?typeId=" + typeId
  }

  @ModelAttribute("type")
  def types(@RequestParam("typeId") typeId: Long) = typeDao.find(typeId)

  @ModelAttribute("form")
  def form = new SearchForm

}

class SearchForm {
  @BeanProperty var status: String = "Live"
  @BeanProperty var name: String = _
}
