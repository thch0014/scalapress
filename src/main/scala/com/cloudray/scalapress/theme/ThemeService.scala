package com.cloudray.scalapress.theme

import org.springframework.stereotype.Component
import com.cloudray.scalapress.obj.Item
import com.cloudray.scalapress.folder.Folder
import org.springframework.beans.factory.annotation.Autowired

@Component
@Autowired
class ThemeService(themeDao: ThemeDao) {

  def theme(folder: Folder) = Option(folder.theme).getOrElse(themeDao.findDefault)
  def theme(obj: Item) = themeDao.findDefault
  def default = themeDao.findDefault
}