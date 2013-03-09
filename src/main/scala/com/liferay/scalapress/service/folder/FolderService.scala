package com.liferay.scalapress.service.folder

import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{ThemeDao, ImageDao, ObjectDao, FolderDao}
import com.liferay.scalapress.domain.{Folder, Obj}
import org.springframework.stereotype.Service

/** @author Stephen Samuel */
trait FolderService {
    def objects(id: Long): Array[Obj]
}

@Service
class FolderServiceImpl extends FolderService {

    @Autowired var folderDao: FolderDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var imageDao: ImageDao = _
    @Autowired var themeDao: ThemeDao = _

    override def objects(id: Long): Array[Obj] = {
        val objects = objectDao.findByFolder(id)
        objects
    }
}
