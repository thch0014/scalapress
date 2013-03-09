package com.liferay.scalapress.controller.admin.obj

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.TypeDao
import com.liferay.scalapress.domain.ObjectType

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("/backoffice/type"))
class TypeListController {

    @Autowired var typeDao: TypeDao = _

    @RequestMapping(produces = Array("text/html"))
    def list(@ModelAttribute t: ObjectType) = "admin/object/type/list.vm"

    import scala.collection.JavaConverters._

    @ModelAttribute("types") def types = typeDao.findAll().asJava
}
