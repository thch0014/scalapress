package com.liferay.scalapress.controller.admin.obj

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{TypeDao, ObjectDao}
import com.liferay.scalapress.{Paging, ScalapressContext}
import com.liferay.scalapress.controller.admin.UrlResolver
import com.liferay.scalapress.domain.Obj
import org.springframework.ui.ModelMap
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/obj"))
class ObjectSearchController {

    @Autowired var objectDao: ObjectDao = _
    @Autowired var typeDao: TypeDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def list = "admin/object/list.vm"

    @RequestMapping(value = Array("create"))
    def create(@RequestParam("typeId") typeId: java.lang.Long): String = {

        val t = typeDao.find(typeId)
        val obj = Obj(t)
        objectDao.save(obj)

        "redirect:" + UrlResolver.objects(typeId)
    }

    @ModelAttribute("type") def types(@RequestParam("typeId") typeId: Long) = typeDao.find(typeId)
    @ModelAttribute def objects(model: ModelMap, @RequestParam("typeId") typeId: Long,
                                @RequestParam(value = "pageNumber", defaultValue = "1") pageNumber: Int,
                                req: HttpServletRequest) {

        val page = objectDao.search(ObjectQuery().withType(typeId).copy(pageNumber = pageNumber))
        model.put("objects", page.java)
        model.put("paging", Paging(req, page))

    }
}
