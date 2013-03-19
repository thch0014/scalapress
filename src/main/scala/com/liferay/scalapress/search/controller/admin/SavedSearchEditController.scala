package com.liferay.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, PathVariable, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.search.{SavedSearch, SavedSearchDao}
import com.liferay.scalapress.folder.FolderDao
import com.liferay.scalapress.obj.TypeDao
import com.liferay.scalapress.theme.MarkupDao
import com.liferay.scalapress.obj.attr.AttributeValue
import com.liferay.scalapress.obj.controller.admin.{FolderPopulator, MarkupPopulator, AttributeValuesPopulator}
import com.liferay.scalapress.util.{ObjectTypePopulator, SortPopulator}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/savedsearch/{id}"))
class SavedSearchEditController
  extends MarkupPopulator with
  SortPopulator with
  FolderPopulator with
  AttributeValuesPopulator with
  ObjectTypePopulator {

    @Autowired var objectTypeDao: TypeDao = _
    @Autowired var folderDao: FolderDao = _
    @Autowired var markupDao: MarkupDao = _
    @Autowired var savedSearchDao: SavedSearchDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("search") search: SavedSearch) = "admin/savedsearch/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("search") search: SavedSearch, req: HttpServletRequest) = {

        search.attributeValues.clear()

        import scala.collection.JavaConverters._
        for (a <- search.objectType.attributes.asScala) {

            val values = req.getParameterValues("attributeValues" + a.id)
            if (values != null) {
                values.map(_.trim).filter(_.length > 0).foreach(value => {
                    val av = new AttributeValue
                    av.attribute = a
                    av.value = value
                    av.savedSearch = search
                    search.attributeValues.add(av)
                })
            }
        }

        savedSearchDao.save(search)
        "redirect:/backoffice/savedsearch/" + search.id
    }

    @ModelAttribute("search") def search(@PathVariable("id") id: Long) = savedSearchDao.find(id)

    import scala.collection.JavaConverters._

    @ModelAttribute("attributesWithValues") def attributesWithValues(@PathVariable("id") id: Long) = {
        val ss = savedSearchDao.find(id)
        if (ss.objectType == null)
            null
        else
            attributeEditMap(ss.objectType.attributes.asScala.toSeq, ss.attributeValues.asScala.toSeq)
    }
}
