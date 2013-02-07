package com.liferay.scalapress.plugin.ecommerce.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, PathVariable, ModelAttribute, RequestMapping}
import com.liferay.scalapress.controller.web.{ScalapressConstants, ScalaPressPage}
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.service.theme.{MarkupRenderer, ThemeService}
import scala.collection.JavaConverters._
import com.liferay.scalapress.dao.ObjectDao
import com.liferay.scalapress.plugin.ecommerce.ShoppingPluginDao
import com.liferay.scalapress.plugin.ecommerce.domain.{BasketLine, Basket}
import com.liferay.scalapress.plugin.ecommerce.dao.BasketDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("basket"))
class BasketController {

    @Autowired var objectDao: ObjectDao = _
    @Autowired var basketDao: BasketDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _
    @Autowired var shoppingPluginDao: ShoppingPluginDao = _

    @ResponseBody
    @RequestMapping(produces = Array("text/html"))
    def view(@ModelAttribute basket: Basket, req: HttpServletRequest): ScalaPressPage = {

        val sreq = ScalapressRequest(req).withTitle("Your Shopping Bag")
        val theme = themeService.default
        val page = ScalaPressPage(theme, req)
        val markup = shoppingPluginDao.get.basketMarkup
        if (markup != null)
            page.body(MarkupRenderer.render(markup, sreq, context))
        page
    }

    @ResponseBody
    @RequestMapping(value = Array("add/{id}"), produces = Array("text/html"))
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

    @ResponseBody
    @RequestMapping(value = Array("remove/{id}"), produces = Array("text/html"))
    def remove(@ModelAttribute basket: Basket, @PathVariable("id") id: Long, req: HttpServletRequest) = {
        basket.lines = basket.lines.asScala.filterNot(_.id == id).asJava
        basketDao.save(basket)
        view(basket, req)
    }

    // populate methods
    @ModelAttribute def basket(req: HttpServletRequest) =
        basketDao.find(req.getAttribute(ScalapressConstants.SessionIdKey).asInstanceOf[String])
}
