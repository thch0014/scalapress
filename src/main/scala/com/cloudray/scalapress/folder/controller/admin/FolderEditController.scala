package com.cloudray.scalapress.folder.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import org.springframework.ui.ModelMap
import scala.collection.JavaConverters._
import com.cloudray.scalapress.util.{UrlGenerator, EnumPopulator, ComponentClassScanner}
import collection.mutable
import com.cloudray.scalapress.section.{SectionDao, Section}
import com.cloudray.scalapress.folder.{FolderOrdering, FolderDao, Folder}
import com.cloudray.scalapress.theme.ThemeDao
import com.cloudray.scalapress.obj.controller.admin.ThemePopulator
import com.cloudray.scalapress.util.mvc.UrlResolver
import com.cloudray.scalapress.media.AssetStore
import scala.Some
import scala.collection.immutable.ListMap
import javax.servlet.http.HttpServletResponse

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/folder/{id}"))
class FolderEditController extends EnumPopulator with ThemePopulator with SectionSorting {

  @Autowired var assetStore: AssetStore = _
  @Autowired var folderDao: FolderDao = _
  @Autowired var themeDao: ThemeDao = _
  @Autowired var sectionDao: SectionDao = _
  @Autowired var context: ScalapressContext = _

  @RequestMapping(value = Array("/section/order"), method = Array(RequestMethod.POST))
  def reorderSections(@RequestBody order: String, @ModelAttribute folder: Folder, response: HttpServletResponse) {
    reorderSections(order, folder.sections.asScala)
    response.setStatus(HttpServletResponse.SC_OK)
  }

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute folder: Folder) = "admin/folder/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute folder: Folder) = {
    if (folder.id == 1)
      folder.parent = null
    folderDao.save(folder)
    edit(folder)
  }

  @RequestMapping(Array("section/create"))
  def createSection(@ModelAttribute folder: Folder, @RequestParam("class") cls: String) = {
    val section = Class.forName(cls).newInstance.asInstanceOf[Section]
    section.folder = folder
    section.name = "new " + Class.forName(cls).getSimpleName
    section.visible = true
    section.init(context)
    folder.sections.add(section)
    folderDao.save(folder)
    "redirect:/backoffice/folder/" + folder.id
  }

  @RequestMapping(value = Array("section/{sectionId}/delete"))
  def deleteSection(@ModelAttribute folder: Folder, @PathVariable("sectionId") sectionId: Long): String = {
    folder.sections.asScala.find(_.id == sectionId) match {
      case None =>
      case Some(section) =>
        folder.sections.remove(section)
        section.folder = null
        folderDao.save(folder)
    }
    "forward:" + UrlResolver.folderEdit(folder)
  }

  @ModelAttribute("parents") def parents = {

    val folders = folderDao.findAll().sortBy(_.id)

    val map = mutable.Map(0l -> "-Default-")
    folders.map(f => {
      map += (f.id -> f.fullName)
    })

    val ordered = ListMap(map.toList.sortBy {
      _._2
    }: _*)

    ordered.asJava
  }

  @ModelAttribute("folderOrderingMap") def folderOrdering = populate(FolderOrdering.values)

  @ModelAttribute def folder(@PathVariable("id") id: Long, map: ModelMap) {
    val folder = folderDao.find(id)
    val sections = folder.sortedSections.asJava
    map.put("eyeball", UrlGenerator.url(folder))
    map.put("folder", folder)
    map.put("sections", sections)
  }

  @ModelAttribute("classes") def classes: java.util.Map[String, String] = {
    val sections = ComponentClassScanner.sections.sortBy(_.getSimpleName)
    val empty = mutable.LinkedHashMap.empty[String, String]
    val map = empty ++: sections.map(c => (c.getName, c.getSimpleName)).toMap
    map.asJava
  }
}