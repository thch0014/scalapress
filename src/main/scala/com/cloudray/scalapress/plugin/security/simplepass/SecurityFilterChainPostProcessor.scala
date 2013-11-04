package com.cloudray.scalapress.plugin.security.simplepass

import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.beans.factory.config.BeanPostProcessor
import scala.collection.JavaConverters._
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component
import com.cloudray.scalapress.framework.Logging

/** @author Stephen Samuel
  *
  *         This processor will remove the BasicAuthenticationFilter form spring security so
  *         that the simplepass plugin can use basic auth without interference.
  **/
@Component
class SecurityFilterChainPostProcessor extends BeanPostProcessor with Logging {

  logger.debug("Registered BeanPostProcessor to remove BasicAuthenticationFilter")

  override def postProcessAfterInitialization(bean: AnyRef, beanName: String): AnyRef = {

    bean match {
      case fc: DefaultSecurityFilterChain =>
        val filters = fc.getFilters.asScala.filterNot(_.isInstanceOf[BasicAuthenticationFilter])
        new DefaultSecurityFilterChain(fc.getRequestMatcher, filters.asJava)
      case _ => bean
    }
  }

  override def postProcessBeforeInitialization(bean: AnyRef, beanName: String): AnyRef = bean
}
