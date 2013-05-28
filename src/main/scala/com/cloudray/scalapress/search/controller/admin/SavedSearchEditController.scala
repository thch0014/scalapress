package com.cloudray.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, PathVariable, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.search.{SavedSearch, SavedSearchDao}
import com.cloudray.scalapress.folder.FolderDao
import com.cloudray.scalapress.obj.TypeDao
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.obj.attr.AttributeValue
import com.cloudray.scalapress.obj.controller.admin.{FolderPopulator, MarkupPopulator}
import com.cloudray.scalapress.util.{AttributePopulator, ObjectTypePopulator, SortPopulator}
import com.cloudray.scalapress.util.mvc.AttributeValuesPopulator
import java.text.SimpleDateFormat
import com.cloudray.scalapress.enums.AttributeType
import scala.collection.JavaConverters._
import org.springframework.ui.ModelMap

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/savedsearch/{id}"))
class SavedSearchEditController
  extends MarkupPopulator with SortPopulator with FolderPopulator
  with AttributePopulator
  with AttributeValuesPopulator with
  ObjectTypePopulator {

    @Autowired var objectTypeDao: TypeDao = _
    @Autowired var folderDao: FolderDao = _
    @Autowired var markupDao: MarkupDao = _
    @Autowired var savedSearchDao: SavedSearchDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("search") search: SavedSearch, model: ModelMap) = {

        if (search.objectType != null) {
            model.put("attributesMap", attributesMap(search.objectType.sortedAttributes))
            model.put("attributesWithValues",
                attributeEditMap(search.objectType.sortedAttributes, search.attributeValues.asScala.toSeq))
        }

        "admin/savedsearch/edit.vm"
    }

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("search") search: SavedSearch, req: HttpServletRequest) = {

        search.attributeValues.clear()
        if (search.objectType != null)
            for (a <- search.objectType.attributes.asScala) {

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

    @ModelAttribute("search") def _search(@PathVariable("id") id: Long) = savedSearchDao.find(id)
}
