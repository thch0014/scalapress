package com.liferay.scalapress.obj.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.sksamuel.scoot.soa.Paging
import org.springframework.ui.ModelMap
import javax.servlet.http.HttpServletRequest
import reflect.BeanProperty
import com.liferay.scalapress.obj.{ObjectDao, TypeDao, Obj}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/obj"))
class ObjectSearchController extends ObjectStatusPopulator {

    @Autowired var objectDao: ObjectDao = _
    @Autowired var typeDao: TypeDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping
    def search(@ModelAttribute("form") form: SearchForm,
             @RequestParam(value = "typeId") typeId: Long,
             @RequestParam(value = "pageNumber", defaultValue = "1") pageNumber: Int,
             model: ModelMap,
             req: HttpServletRequest) = {

        val query = new ObjectQuery
        query.pageNumber = pageNumber
        query.pageSize = 20
        query.status = Option(form.status)
        query.name = Option(form.name)
        query.typeId = Option(typeId).filter(_ > 0)

        val page = objectDao.search(query)
        model.put("objects", page.java)
        model.put("paging", Paging(req, page))

        "admin/object/list.vm"
    }

    @RequestMapping(value = Array("create"))
    def create(@RequestParam("typeId") typeId: java.lang.Long): String = {

        val t = typeDao.find(typeId)
        val obj = Obj(t)
        objectDao.save(obj)

        "redirect:/backoffice/obj/" + obj.id
    }

    @ModelAttribute("type") def types(@RequestParam("typeId") typeId: Long) = typeDao.find(typeId)
    @ModelAttribute("form") def form = new SearchForm

}

class SearchForm {
    @BeanProperty var status: String = "Live"
    @BeanProperty var name: String = _
}
