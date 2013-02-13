package com.liferay.scalapress.controller.admin.obj

import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.ui.ModelMap
import com.liferay.scalapress.dao.{FolderDao, ThemeDao, MarkupDao}
import scala.collection.JavaConverters._
import collection.immutable.TreeMap
import com.liferay.scalapress.plugin.ecommerce.ShoppingPluginDao

/** @author Stephen Samuel */
trait MarkupPopulator {

    var markupDao: MarkupDao

    @ModelAttribute def markups(model: ModelMap) {
        val markups = markupDao.findAll()

        var map = TreeMap(0l -> "-None-")
        markups.map(markup => {
            map = map + (markup.id -> ("#" + markup.id + " " + markup.name))
        })

        model.put("markups", markups.asJava)
        model.put("markupMap", map.asJava)
    }
}

trait OrderStatusPopulator {

    var shoppingPluginDao: ShoppingPluginDao

    @ModelAttribute def markups(model: ModelMap) {

        var map = TreeMap("" -> "-Change Status-")

        val statuses = Option(shoppingPluginDao.get.statuses).getOrElse("")
        statuses.split("\n").filter(_.trim.length > 0).foreach(status => {
            map = map + ((status -> status))
        })

        model.put("statusMap", map.asJava)
    }
}

trait ThemePopulator {

    var themeDao: ThemeDao

    @ModelAttribute def themes(model: ModelMap) {
        val themes = themeDao.findAll()

        var map = TreeMap(0l -> "-None-")
        themes.map(theme => {
            map = map + (theme.id -> ("#" + theme.id + " " + theme.name))
        })

        model.put("themes", themes.asJava)
        model.put("themesMap", map.asJava)
    }
}

trait FolderPopulator {

    var folderDao: FolderDao

    @ModelAttribute def themes(model: ModelMap) {
        val folders = folderDao.findAll()

        var map = TreeMap(0l -> "-Default-")
        folders.map(f => {
            map = map + (f.id -> ("#" + f.id + " " + f.fullName))
        })

        model.put("folders", folders.asJava)
        model.put("foldersMap", map.asJava)
    }
}


