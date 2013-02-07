package com.liferay.scalapress.controller.admin.folder

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import com.liferay.scalapress.domain.{Image, Folder}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.FolderDao
import com.liferay.scalapress.ScalapressContext
import org.springframework.web.multipart.MultipartFile
import com.liferay.scalapress.controller.admin.UrlResolver
import com.liferay.scalapress.service.asset.AssetStore
import org.springframework.ui.ModelMap
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/folder/{id}"))
class FolderEditController {

    @Autowired var assetStore: AssetStore = _
    @Autowired var folderDao: FolderDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute folder: Folder) = "admin/folder/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute folder: Folder) = {
        folderDao.save(folder)
        edit(folder)
    }

    @RequestMapping(value = Array("upload"), method = Array(RequestMethod.POST))
    def upload(@ModelAttribute folder: Folder, @RequestParam("upload") upload: MultipartFile): String = {

        val key = assetStore.add(upload.getOriginalFilename, upload.getInputStream)

        val image = new Image
        image.filename = key
        image.date = System.currentTimeMillis()
        image.folder = folder
        folder.images.add(image)

        folderDao.save(folder)
        "redirect:" + UrlResolver.folderEdit(folder)
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

        val folders = folderDao.findAll().map(f => (f.id, f.fullName))
        val options = (0, "-None-") +: folders
        val java = options.toMap.asJava
        java
    }

    @ModelAttribute def folder(@PathVariable("id") id: Long, map: ModelMap) {
        val folder = folderDao.find(id)
        val sections = folder.sections
        map.put("eyeball", UrlResolver.folderSiteView(folder))
        map.put("folder", folder)
        map.put("sections", sections)
    }
}