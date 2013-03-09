package com.liferay.scalapress.service.theme

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.ServletContext
import com.liferay.scalapress.dao.ThemeDao
import com.liferay.scalapress.domain.{Obj, Folder}

@Service
class ThemeService {

    @Autowired
    var servletContext: ServletContext = _

    @Autowired
    var storageProvider: ThemeStorageProvider = _

    @Autowired var themeDao: ThemeDao = _

    def theme(folder: Folder) = themeDao.findDefault
    def theme(obj: Obj) = themeDao.findDefault
    def default = themeDao.findDefault

}