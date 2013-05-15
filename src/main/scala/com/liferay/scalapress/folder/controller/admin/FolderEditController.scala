package com.liferay.scalapress.folder.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestBody, RequestParam, RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import org.springframework.ui.ModelMap
import scala.collection.JavaConverters._
import com.liferay.scalapress.enums.FolderOrdering
import com.liferay.scalapress.util.{EnumPopulator, ComponentClassScanner}
import collection.mutable
import com.liferay.scalapress.section.{SectionDao, Section}
import com.liferay.scalapress.folder.{FolderDao, Folder}
import com.liferay.scalapress.theme.ThemeDao
import com.liferay.scalapress.obj.controller.admin.ThemePopulator
import com.liferay.scalapress.util.mvc.UrlResolver
import com.liferay.scalapress.media.AssetStore

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/folder/{id}"))
class FolderEditController extends EnumPopulator with ThemePopulator {

    @Autowired var assetStore: AssetStore = _
    @Autowired var folderDao: FolderDao = _
    @Autowired var themeDao: ThemeDao = _
    @Autowired var sectionDao: SectionDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute folder: Folder) = "admin/folder/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute folder: Folder) = {
        if (folder.id == 1l)
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

    //    @RequestMapping(value = Array("upload"), method = Array(RequestMethod.POST))
    //    def upload(@ModelAttribute folder: Folder, @RequestParam("upload") upload: MultipartFile): String = {
    //
    //        val key = assetStore.add(upload.getOriginalFilename, upload.getInputStream)
    //
    //        val image = new Image
    //        image.filename = key
    //        image.date = System.currentTimeMillis()
    //        image.folder = folder
    //        folder.images.add(image)
    //
    //        folderDao.save(folder)
    //        "redirect:" + UrlResolver.folderEdit(folder)
    //    }

    @RequestMapping(value = Array("/section/order"), method = Array(RequestMethod.POST))
    def reorderSections(@RequestBody order: String, @ModelAttribute folder: Folder): String = {

        val ids = order.split("-")
        folder.sections.asScala.foreach(section => {
            val pos = ids.indexOf(section.id.toString)
            section.position = pos
            sectionDao.save(section)
        })
        "ok"
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

    @ModelAttribute("parents") def folder = {

        val folders = folderDao.tree

        val map = new mutable.LinkedHashMap[Long, String]
        map.put(0, "-None-")
        for (folder <- folders)
            map.put(folder.id, folder.fullName)
        map.asJava
    }

    @ModelAttribute("folderOrderingMap") def folderOrdering = populate(FolderOrdering.values)

    @ModelAttribute def folder(@PathVariable("id") id: Long, map: ModelMap) {
        val folder = folderDao.find(id)
        val sections = folder.sections.asScala.toSeq.sortBy(_.position).asJava
        map.put("eyeball", UrlResolver.folderSiteView(folder))
        map.put("folder", folder)
        map.put("sections", sections)
    }

    @ModelAttribute("classes") def classes: java.util.Map[String, String] = {
        val sections = ComponentClassScanner.sections.sortBy(_.getSimpleName)
        val empty = mutable.LinkedHashMap.empty[String, String]
        val map = empty ++ sections.map(c => (c.getName, c.getSimpleName)).toMap
        map.asJava
    }
}