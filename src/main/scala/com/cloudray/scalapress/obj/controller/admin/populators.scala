package com.cloudray.scalapress.obj.controller.admin

import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.ui.ModelMap
import scala.collection.JavaConverters._
import collection.immutable.TreeMap
import com.cloudray.scalapress.plugin.ecommerce.{ShoppingPlugin, ShoppingPluginDao}
import com.cloudray.scalapress.plugin.ecommerce.dao.{AddressDao, DeliveryOptionDao}
import com.cloudray.scalapress.plugin.ecommerce.domain.Address
import com.googlecode.genericdao.search.Search
import com.cloudray.scalapress.plugin.form.FormDao
import scala.collection.mutable
import com.cloudray.scalapress.folder.FolderDao
import com.cloudray.scalapress.theme.{MarkupDao, ThemeDao}

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

trait FormPopulator {

    var formDao: FormDao

    @ModelAttribute def forms(model: ModelMap) {
        val forms = formDao.findAll()

        var map = TreeMap(0l -> "-None-")
        forms.map(f => {
            map = map + (f.id -> ("#" + f.id + " " + f.name))
        })

        model.put("forms", forms.asJava)
        model.put("formsMap", map.asJava)
    }
}

trait OrderStatusPopulator {

    var shoppingPluginDao: ShoppingPluginDao

    @ModelAttribute def orderStatusMap(model: ModelMap) {
        val map = _statuses
        model.put("statusMap", map.asJava)
        model.put("orderStatusMap", map.asJava)
    }

    def _statuses: mutable.LinkedHashMap[String, String] = {

        val statuses = Option(shoppingPluginDao.get.statuses).getOrElse("").split(Array(',', '\n')) ++ ShoppingPlugin.defaultStatuses
        val set = statuses.toSet[String]
        val seq = set.toIndexedSeq
        val sorted = seq.sortWith((a, b) => a.compareToIgnoreCase(b) < 0)

        val map = mutable.LinkedHashMap("" -> "-Status-")
        sorted.filterNot(_.isEmpty).foreach(status => {
            map.put(status.trim, status.trim)
        })
        map
    }
}

trait ObjectStatusPopulator {

    @ModelAttribute def objectStatusMap(model: ModelMap) {
        val map = mutable
          .LinkedHashMap("" -> "-Status-", "Live" -> "Live", "Disabled" -> "Hidden", "Deleted" -> "Deleted")
        model.put("objectStatusMap", map.asJava)
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

    @ModelAttribute def folders(model: ModelMap) {
        val folders = folderDao.findAll()

        val map = mutable.LinkedHashMap.empty[Long, String]
        map.put(0l, "-Default-")
        folders.map(f => {
            map.put(f.id, "#" + f.id + " " + f.fullName)
        })

        model.put("folders", folders.asJava)
        model.put("foldersMap", map.asJava)
    }
}

trait DeliveryOptionPopulator {

    var deliveryOptionDao: DeliveryOptionDao

    @ModelAttribute def deliveryOptions(model: ModelMap) {
        val opts = deliveryOptionDao.findAll().sortBy(d => Option(d.name).getOrElse(""))

        var map = TreeMap(0l -> "-Select Delivery-")
        opts.map(o => {
            val price = "&pound;%1.2f".format(o.charge / 100.0)
            map = map + (o.id -> (o.name + " " + price))
        })

        model.put("deliveryOptions", opts.asJava)
        model.put("deliveryOptionsMap", map.asJava)
    }
}

trait AddressPopulator {

    var addressDao: AddressDao

    def addressOptions(accountId: Long, model: ModelMap) {
        val opts = addressDao.search(new Search(classOf[Address])
          .addFilterEqual("account", accountId)
          .addFilterEqual("active", true))

        var map = TreeMap(0l -> "-Select Address-")
        opts.map(o => {
            map = map + (o.id -> (o.name + " " + o.address1 + " " + o.town + " " + o.postcode))
        })

        model.put("addresses", opts.asJava)
        model.put("addressesMap", map.asJava)
    }
}