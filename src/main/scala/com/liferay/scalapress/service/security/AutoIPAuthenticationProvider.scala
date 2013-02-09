package com.liferay.scalapress.service.security

import org.springframework.security.authentication.{UsernamePasswordAuthenticationToken, AuthenticationProvider}
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import org.apache.commons.io.IOUtils
import scala.collection.JavaConverters._
import org.springframework.security.web.authentication.WebAuthenticationDetails

/** @author Stephen Samuel */
@Component
class AutoIPAuthenticationProvider extends AuthenticationProvider {

    var ips: Array[String] = _

    @PostConstruct
    def loadIps() {

        ips = IOUtils.readLines(getClass.getResourceAsStream("/autoip")).asScala.toArray
    }

    def supports(authentication: Class[_]): Boolean = true

    def authenticate(auth: Authentication): Authentication = {
        val web = auth.getDetails.asInstanceOf[WebAuthenticationDetails]
        val ip = web.getRemoteAddress

        if (ip == "127.0.0.1" || ip == "localhost")
            new UsernamePasswordAuthenticationToken("admin", "admin", List(AdminAuthority).asJava)
        else
            auth
    }
}
