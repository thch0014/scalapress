package com.cloudray.scalapress.plugin.ecommerce.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters._
import com.cloudray.scalapress.plugin.ecommerce.ShoppingPluginDao
import com.cloudray.scalapress.plugin.ecommerce.domain.{BasketLine, Basket}
import com.cloudray.scalapress.plugin.ecommerce.dao.BasketDao
import com.cloudray.scalapress.obj.ObjectDao
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.theme.{ThemeService, MarkupRenderer}
import scala.Some
import com.cloudray.scalapress.plugin.variations.VariationDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("basket"))
class BasketController {

  @Autowired var variationDao: VariationDao = _
  @Autowired var objectDao: ObjectDao = _
  @Autowired var basketDao: BasketDao = _
  @Autowired var context: ScalapressContext = _
  @Autowired var themeService: ThemeService = _
  @Autowired var shoppingPluginDao: ShoppingPluginDao = _

  @ResponseBody
  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
  def view(@ModelAttribute basket: Basket, req: HttpServletRequest): ScalapressPage = {

    val sreq = ScalapressRequest(req, context).withTitle("Your Shopping Bag")
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)
    val markup = shoppingPluginDao.get.basketMarkup


    page.body("<form method='POST'>")
    if (markup == null) {
      page.body("<!-- no basket markup set -->")
    } else {
      val obj = Option(req.getParameter("objectId")).flatMap(objId => Option(objectDao.find(objId.toLong)))
      page.body(MarkupRenderer.render(markup, obj.map(sreq.withObject).getOrElse(sreq)))
    }

    page.body("</form>")
    page
  }

  @RequestMapping(method = Array(RequestMethod.POST))
  def update(@ModelAttribute basket: Basket, req: HttpServletRequest): String = {
    basket.lines.asScala.foreach(line => {
      val qty = req.getParameter("qty" + line.id).toInt
      line.qty = qty
    })
    basketDao.save(basket)
    "redirect:/basket"
  }

  @RequestMapping(value = Array("add/{id}"), produces = Array("text/html"))
  def add(@ModelAttribute basket: Basket,
          @PathVariable("id") id: Long,
          @RequestParam(value = "variation", defaultValue = "0", required = false) variationId: Long) = {

    val obj = objectDao.find(id)
    val line = new BasketLine
    line.obj = obj
    line.qty = 1
    line.basket = basket
    line.variation = variationDao.find(variationId)

    basket.lines.add(line)
    basketDao.save(basket)
    "redirect:/basket?objectId=" + id
  }

  @RequestMapping(value = Array("remove/{id}"), produces = Array("text/html"))
  def remove(@ModelAttribute basket: Basket, @PathVariable("id") id: Long) = {
    basket.lines.asScala.find(_.id == id) match {
      case None =>
      case Some(line) =>
        basket.lines.remove(line)
        line.basket = null

        basketDao.save(basket)
    }
    "redirect:/basket"
  }

  @ModelAttribute def basket(req: HttpServletRequest) = ScalapressRequest(req, context).basket
}
