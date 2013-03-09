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

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/folder/{id}"))
class FolderEditController {

    @Autowired var assetStore: AssetStore = _
    @Autowired var folderDao: FolderDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute folder: Folder) = "admin/folder/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute folder: Folder) = {
        folderDao.save(folder)
        edit(folder)
    }

    @RequestMapping(produces = Array("text/html"), value = Array("upload"), method = Array(RequestMethod.POST))
    def upload(@ModelAttribute folder: Folder, @RequestParam("upload") upload: MultipartFile): String = {

        val key = assetStore.put(upload.getOriginalFilename, upload.getInputStream)

        val image = new Image
        image.filename = key
        image.date = System.currentTimeMillis()
        image.folder = folder
        folder.images.add(image)

        folderDao.save(folder)
        "redirect:" + UrlResolver.folderEdit(folder)
    }

    import scala.collection.JavaConverters._

    @ModelAttribute("parents") def folder = {

        val folders = folderDao.findAll().map(f => (f.id, f.fullName))
        val options = (0, "-Select-") +: folders
        val java = options.toMap.asJava
        java
    }

    @ModelAttribute def folder(@PathVariable("id") id: Long, map: ModelMap) {
        val folder = folderDao.find(id)
        val sections = folder.sections
        map.put("folder", folder)
        map.put("sections", sections)
    }
}
