package com.liferay.scalapress.controller.web.ecommerce

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMapping}
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.ScalapressContext
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.dao.ecommerce.BasketDao
import com.liferay.scalapress.domain.ecommerce.{BasketLine, Basket}
import com.liferay.scalapress.dao.ObjectDao
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("basket"))
class BasketController {

    @Autowired var basketDao: BasketDao = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _

    @RequestMapping
    def view(@ModelAttribute basket: Basket, req: HttpServletRequest): ScalaPressPage = {
        val theme = themeService.default
        val page = ScalaPressPage(theme, req)
        val basket = basketDao.find(req.getAttribute(ScalapressConstants.SessionIdKey).asInstanceOf[String])
        page.body(BasketRenderer.render(basket))
        page
    }

    @RequestMapping(Array("add/{id}"))
    def add(@ModelAttribute basket: Basket, @PathVariable("id") id: Long, req: HttpServletRequest) = {
        val obj = objectDao.find(id)
        val line = new BasketLine
        line.obj = obj
        line.qty = 1
        line.basket = basket
        basket.lines.add(line)
        basketDao.save(basket)
        view(basket, req)
    }

    @RequestMapping(Array("remove/{id}"))
    def remove(@ModelAttribute basket: Basket, @PathVariable("id") id: Long, req: HttpServletRequest) = {
        basket.lines = basket.lines.asScala.filterNot(_.id == id).asJava
        basketDao.save(basket)
        view(basket, req)
    }

    // populate methods
    @ModelAttribute def basket(req: HttpServletRequest) =
        basketDao.find(req.getAttribute(ScalapressConstants.SessionIdKey).asInstanceOf[String])
}
