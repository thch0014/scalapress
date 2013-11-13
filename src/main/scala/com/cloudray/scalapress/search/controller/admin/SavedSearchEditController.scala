package com.cloudray.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, PathVariable, RequestMapping}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.search.{SavedSearch, SavedSearchDao}
import com.cloudray.scalapress.folder.FolderDao
import com.cloudray.scalapress.item.TypeDao
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.item.attr.{AttributeType, AttributeValue}
import com.cloudray.scalapress.item.controller.admin.{FolderPopulator, MarkupPopulator}
import com.cloudray.scalapress.util.{AttributePopulator, ItemTypePopulator, SortPopulator}
import com.cloudray.scalapress.util.mvc.AttributeValuesPopulator
import java.text.SimpleDateFormat
import scala.collection.JavaConverters._
import org.springframework.ui.ModelMap
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/savedsearch/{id}"))
@Autowired
class SavedSearchEditController(val itemTypeDao: TypeDao,
                                val folderDao: FolderDao,
                                val markupDao: MarkupDao,
                                val savedSearchDao: SavedSearchDao)
  extends MarkupPopulator with SortPopulator with FolderPopulator
  with AttributePopulator
  with AttributeValuesPopulator with
  ItemTypePopulator {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("search") search: SavedSearch, model: ModelMap) = {

    if (search.itemType != null) {
      model.put("attributesMap", attributesMap(search.itemType.sortedAttributes))
      model.put("attributesWithValues",
        attributeEditMap(search.itemType.sortedAttributes, search.attributeValues.asScala.toSeq))
    }

    "admin/savedsearch/edit.vm"
  }

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("search") search: SavedSearch, req: HttpServletRequest) = {

    search.attributeValues.clear()
    if (search.itemType != null)
      for ( a <- search.itemType.attributes.asScala ) {

        val values = req.getParameterValues("attributeValues" + a.id)
        if (values != null) {
          values.map(_.trim).filter(_.length > 0).foreach(value => {
            val av = new AttributeValue
            av.attribute = a
            // some values need to be converted first
            av.attribute.attributeType match {
              case AttributeType.Date => new SimpleDateFormat("dd-MM-yyyy").parse(value).getTime
              case _ => av.value = value
            }
            av.savedSearch = search
            search.attributeValues.add(av)
          })
        }
      }

    savedSearchDao.save(search)
    "redirect:/backoffice/savedsearch/" + search.id
  }

  @ModelAttribute("search")
  def _search(@PathVariable("id") id: Long) = savedSearchDao.find(id)
}
