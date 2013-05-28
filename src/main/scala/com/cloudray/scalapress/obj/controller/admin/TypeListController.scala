package com.cloudray.scalapress.obj.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.googlecode.genericdao.search.Search
import com.cloudray.scalapress.obj.{TypeDao, ObjectType}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("/backoffice/type"))
class TypeListController {

    @Autowired var typeDao: TypeDao = _

    @RequestMapping(produces = Array("text/html"))
    def list(@ModelAttribute t: ObjectType) = "admin/object/type/list.vm"

    @RequestMapping(produces = Array("text/html"), value = Array("create"))
    def create = {
        val t = new ObjectType
        t.name = "new object type"
        typeDao.save(t)
        "redirect:/backoffice/type"
    }

    import scala.collection.JavaConverters._

    @ModelAttribute("types") def types = typeDao
      .search(new Search(classOf[ObjectType]).addFilterEqual("deleted", false)).asJava
}
