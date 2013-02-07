package com.liferay.scalapress.plugin.ecommerce

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMapping}
import com.liferay.scalapress.controller.web.{ScalapressConstants, ScalaPressPage}
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.service.theme.{MarkupRenderer, ThemeService}
import com.liferay.scalapress.dao.ecommerce.BasketDao
import com.liferay.scalapress.domain.ecommerce.{BasketLine, Basket}
import scala.collection.JavaConverters._
import com.liferay.scalapress.dao.ObjectDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("basket"))
class BasketController {

    @Autowired var objectDao: ObjectDao = _
    @Autowired var basketDao: BasketDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _
    @Autowired var shoppingPluginDao: ShoppingPluginDao = _

    @RequestMapping
    def view(@ModelAttribute basket: Basket, req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req)
        val theme = themeService.default
        val page = ScalaPressPage(theme, req)
        val markup = shoppingPluginDao.get.basketMarkup

        page.body("<h1>Your Shopping Bag</h1>")
        if (markup != null)
            page.body(MarkupRenderer.render(markup, sreq, context))
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
