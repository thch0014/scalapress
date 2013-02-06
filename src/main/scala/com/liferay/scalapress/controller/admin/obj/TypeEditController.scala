package com.liferay.scalapress.controller.admin.obj

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{MarkupDao, TypeDao}
import scala.Array
import com.liferay.scalapress.domain.ObjectType
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.controller.admin.UrlResolver
import com.liferay.scalapress.domain.attr.Attribute
import org.springframework.ui.ModelMap
import com.liferay.scalapress.enums.AttributeType

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/type/{id}"))
class TypeEditController {

    @Autowired var typeDao: TypeDao = _
    @Autowired var markupDao: MarkupDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute("type") t: ObjectType) = "admin/object/type/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute("type") t: ObjectType) = {
        typeDao.save(t)
        edit(t)
    }

    @RequestMapping(Array("/attribute/create"))
    def createAttribute(@ModelAttribute("type") t: ObjectType) = {

        val attribute = new Attribute
        attribute.name = "new attribute"
        attribute.attributeType = AttributeType.Text
        attribute.objectType = t
        t.attributes.add(attribute)

        typeDao.save(t)

        "redirect:" + UrlResolver.typeEdit(t)
    }

    @ModelAttribute def t(@PathVariable("id") id: java.lang.Long, model: ModelMap) {

        import scala.collection.JavaConverters._

        val t = typeDao.find(id)
        val sortedAttributes = t.attributes.asScala.sortBy(arg => Option(arg.section).getOrElse("")).asJava

        model.put("type", t)
        model.put("attributes", sortedAttributes)

        val markups = markupDao.findAll()
        val map = markups.map(m => (m.id, m.name)).toMap + ((0, "-None-"))
        model.put("markups", markups.asJava)
        model.put("markupMap", map.asJava)
    }
}
