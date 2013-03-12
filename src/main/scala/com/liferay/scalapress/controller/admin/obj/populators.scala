package com.liferay.scalapress.controller.admin.obj

import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.ui.ModelMap
import com.liferay.scalapress.dao.{FolderDao, ThemeDao, MarkupDao}
import scala.collection.JavaConverters._
import collection.immutable.TreeMap
import com.liferay.scalapress.plugin.ecommerce.ShoppingPluginDao
import com.liferay.scalapress.plugin.ecommerce.dao.{AddressDao, DeliveryOptionDao}
import com.liferay.scalapress.plugin.ecommerce.domain.Address
import com.googlecode.genericdao.search.Search
import com.liferay.scalapress.plugin.form.FormDao
import collection.mutable

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

        var map = TreeMap("" -> "-Status-")

        val statuses = Option(shoppingPluginDao.get.statuses).getOrElse("")
        statuses.split("\n").filter(_.trim.length > 0).foreach(status => {
            map = map + ((status -> status))
        })

        model.put("statusMap", map.asJava)
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

        var map = TreeMap(0l -> "-Default-")
        folders.map(f => {
            map = map + (f.id -> ("#" + f.id + " " + f.fullName))
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