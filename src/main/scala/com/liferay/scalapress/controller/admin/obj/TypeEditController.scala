package com.liferay.scalapress.controller.admin.obj

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.TypeDao
import scala.Array
import com.liferay.scalapress.domain.{AttributeType, ObjectType}
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.controller.admin.UrlResolver
import com.liferay.scalapress.domain.attr.Attribute
import org.springframework.ui.ModelMap

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/type/{id}"))
class TypeEditController {

    @Autowired var typeDao: TypeDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute t: ObjectType) = "admin/object/type/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute t: ObjectType) = {
        typeDao.save(t)
        edit(t)
    }

    @RequestMapping(Array("/attribute/create"))
    def createAttribute(@ModelAttribute t: ObjectType) = {

        val attribute = new Attribute
        attribute.name = "new attribute"
        attribute.attributeType = AttributeType.Text
        attribute.objectType = t
        t.attributes.add(attribute)

        typeDao.save(t)

        "redirect:" + UrlResolver.typeEdit(t)
    }

    @ModelAttribute def t(@PathVariable("id") id: java.lang.Long, model: ModelMap) = {

        import scala.collection.JavaConverters._

        val t = typeDao.find(id)
        val sortedAttributes = t.attributes.asScala.sortBy(arg => Option(arg.section).getOrElse("")).asJava

        model.put("type", t)
        model.put("attributes", sortedAttributes)
    }
}
