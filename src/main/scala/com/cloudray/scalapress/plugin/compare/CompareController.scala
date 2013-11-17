package com.cloudray.scalapress.plugin.compare

import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.item.{Item, ItemDao}
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.framework.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("compare"))
class CompareController(itemDao: ItemDao,
                        themeService: ThemeService,
                        context: ScalapressContext) {

  @ResponseBody
  @RequestMapping(produces = Array("text/html"))
  def compare(req: HttpServletRequest): ScalapressPage = {

    val items = req
      .getParameterValues("id")
      .map(id => itemDao.find(id.toLong))
      .filter(_.status.toLowerCase == Item.STATUS_LIVE_LOWER)

    val sreq = ScalapressRequest(req, context)
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)
    page.body(new CompareRenderer(context.assetStore).render(items))
    page
  }
}
