package com.cloudray.scalapress.account.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import javax.servlet.http.HttpServletRequest
import org.springframework.validation.Errors
import com.cloudray.scalapress.theme.{ThemeService, ThemeDao}
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.security.{SecurityResolver, SpringSecurityResolver}
import org.apache.commons.lang.StringUtils
import org.springframework.security.authentication.encoding.PasswordEncoder
import com.cloudray.scalapress.account.{AccountPluginDao, Account, AccountDao}
import com.cloudray.scalapress.account.controller.renderer.ProfileRenderer
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("profile"))
class ProfileController {

  @Autowired var accountDao: AccountDao = _
  @Autowired var themeDao: ThemeDao = _
  @Autowired var themeService: ThemeService = _
  @Autowired var accountPluginDao: AccountPluginDao = _
  @Autowired var context: ScalapressContext = _
  @Autowired var passwordEncoder: PasswordEncoder = _
  var securityResolver: SecurityResolver = SpringSecurityResolver

  @ResponseBody
  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def show(req: HttpServletRequest, @ModelAttribute profile: Profile, errors: Errors): ScalapressPage = {

    val plugin = accountPluginDao.get
    val sreq = ScalapressRequest(req, context).withTitle("Your Profile")
    val theme = themeService.default
    val page = ScalapressPage(theme, sreq)
    page.body(new ProfileRenderer().render(profile, errors))
    page
  }

  @RequestMapping(method = Array(RequestMethod.POST))
  def update(req: HttpServletRequest, @ModelAttribute profile: Profile, errors: Errors): String = {
    if (profile.email != null)
      if (profile.name != null) {
        val account = securityResolver.getAccount(req).get
        account.name = profile.name
        account.email = profile.email
        if (StringUtils.isNotBlank(profile.password)) {
          account.passwordHash = passwordEncoder.encodePassword(profile.password, null)
        }
        accountDao.save(account)
      }
    "redirect:profile"
  }

  @ModelAttribute def profile(req: HttpServletRequest) = Profile(securityResolver.getAccount(req).get)
}

class Profile {
  var name: String = _
  var email: String = _
  var password: String = _
}

object Profile {
  def apply(obj: Account): Profile = {
    val profile = new Profile
    profile.name = obj.name
    profile.email = obj.email
    profile
  }
}