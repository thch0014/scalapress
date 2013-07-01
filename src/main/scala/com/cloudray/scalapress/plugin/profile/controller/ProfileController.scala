package com.cloudray.scalapress.plugin.profile.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.plugin.profile.AccountPluginDao
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import scala.Array
import javax.servlet.http.HttpServletRequest
import org.springframework.validation.Errors
import com.cloudray.scalapress.obj.{ObjectDao, Obj}
import com.cloudray.scalapress.theme.{ThemeService, ThemeDao}
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.security.{SecurityResolver, SpringSecurityResolver}
import com.cloudray.scalapress.plugin.profile.controller.renderer.ProfileRenderer
import org.apache.commons.lang.StringUtils
import org.springframework.security.authentication.encoding.PasswordEncoder

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("profile"))
class ProfileController {

    @Autowired var objectDao: ObjectDao = _
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
        page.body(ProfileRenderer.renderProfilePage(profile, plugin, errors))
        page
    }

    @RequestMapping(method = Array(RequestMethod.POST))
    def update(req: HttpServletRequest, @ModelAttribute profile: Profile, errors: Errors): String = {
        if (profile.email != null)
            if (profile.name != null) {
                val account = securityResolver.getUser(req).get
                account.name = profile.name
                account.email = profile.email
                if (StringUtils.isNotBlank(profile.password)) {
                    account.passwordHash = passwordEncoder.encodePassword(profile.password, null)
                }
                objectDao.save(account)
            }
        "redirect:profile"
    }

    @ModelAttribute def profile(req: HttpServletRequest) = Profile(securityResolver.getUser(req).get)
}

class Profile {
    var name: String = _
    var email: String = _
    var password: String = _
}

object Profile {
    def apply(obj: Obj): Profile = {
        val profile = new Profile
        profile.name = obj.name
        profile.email = obj.email
        profile
    }
}