package com.cloudray.scalapress.theme

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.ServletContext
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder

@Service
class ThemeService {

  @Autowired var servletContext: ServletContext = _
  @Autowired var themeDao: ThemeDao = _

  def theme(folder: Folder) = Option(folder.theme).getOrElse(themeDao.findDefault)
  def theme(obj: Obj) = themeDao.findDefault
  def default = themeDao.findDefault
}