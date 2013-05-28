package com.cloudray.scalapress.plugin.variations

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(value = Array("/backoffice/plugin/variations/dimensions"))
class DimensionListController {

    @Autowired var dimensionDao: DimensionDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping
    def list = "admin/plugin/gallery/dimensions/list.vm"

    @ModelAttribute("dimensions") def dimensions = dimensionDao.findAll().asJava
}
