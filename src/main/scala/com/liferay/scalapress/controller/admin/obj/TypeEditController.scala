package com.liferay.scalapress.controller.admin.obj

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestBody, PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{MarkupDao, TypeDao}
import scala.Array
import com.liferay.scalapress.domain.ObjectType
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.controller.admin.UrlResolver
import com.liferay.scalapress.domain.attr.Attribute
import org.springframework.ui.ModelMap
import com.liferay.scalapress.enums.AttributeType
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/type/{id}"))
class TypeEditController extends MarkupPopulator {

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

    @RequestMapping(value = Array("/attribute/order"), method = Array(RequestMethod.POST))
    def reorderAttributes(@RequestBody order: String, @ModelAttribute("type") t: ObjectType): String = {

        val ids = order.split("-")
        t.attributes.asScala.foreach(a => {
            val pos = ids.indexOf(a.id.toString)
            a.position = pos
            context.attributeDao.save(a)
        })
        "ok"
    }

    @ModelAttribute def t(@PathVariable("id") id: java.lang.Long, model: ModelMap) {

        import scala.collection.JavaConverters._

        val t = typeDao.find(id)
        val sortedAttributes = t.attributes.asScala.sortBy(_.position).asJava

        model.put("type", t)
        model.put("attributes", sortedAttributes)
    }
}
