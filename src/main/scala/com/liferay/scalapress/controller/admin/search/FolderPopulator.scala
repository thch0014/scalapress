package com.liferay.scalapress.controller.admin.search

import org.springframework.web.bind.annotation.ModelAttribute
import com.liferay.scalapress.dao.FolderDao
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
trait FolderPopulator {

    var folderDao: FolderDao

    @ModelAttribute("foldersMap") def folder = {

        val folders = folderDao.findAll().map(f => (f.id, f.fullName))
        val options = ("", "-None-") +: folders
        val java = options.toMap.asJava
        java
    }
}
