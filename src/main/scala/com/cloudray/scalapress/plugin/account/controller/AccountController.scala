package com.cloudray.scalapress.plugin.account.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import com.cloudray.scalapress.theme.{MarkupRenderer, ThemeService}
import com.cloudray.scalapress.plugin.account.{AccountLink, AccountPluginDao}
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.util.ComponentClassScanner
import com.cloudray.scalapress.account.Account
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("account"))
@Autowired
class AccountController(themeService: ThemeService,
                        accountPluginDao: AccountPluginDao,
                        context: ScalapressContext) {

  var links: Seq[Class[_ <: AccountLink]] = new ComponentClassScanner().getSubtypes(classOf[AccountLink])

  @ResponseBody
  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def show(req: HttpServletRequest, @ModelAttribute("account") account: Account): ScalapressPage = {

    val plugin = accountPluginDao.get

    val sreq = ScalapressRequest(req, context).withTitle("Your Account")
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)

    page.body(plugin.accountPageHeader)

    Option(plugin.accountPageMarkup) match {
      case None => page.body(AccountRenderer.links(links, context))
      case Some(m) => page.body(MarkupRenderer.render(m, sreq))
    }

    page.body(plugin.accountPageFooter)
    page
  }
}
