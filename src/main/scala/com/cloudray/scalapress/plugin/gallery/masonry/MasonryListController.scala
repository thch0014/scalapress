package com.cloudray.scalapress.plugin.gallery.masonry

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.section.Section

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/gallery/masonry"))
class MasonryListController {

  @Autowired var context: ScalapressContext = _

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit = "admin/plugin/gallery/masonry/section/list.vm"

  @ModelAttribute("sections") def sections: Array[Section] = {
    context.sectionDao.findAll.filter(_.isInstanceOf[MasonrySection]).toArray
  }
}
