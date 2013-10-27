package com.cloudray.scalapress.plugin.security.simplepass

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import org.springframework.security.web.authentication.www.{DigestAuthenticationFilter, BasicAuthenticationFilter}
import org.springframework.security.web.DefaultSecurityFilterChain
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
class SecurityFilterChainPostProcessorTest extends FlatSpec with OneInstancePerTest with MockitoSugar {

  val processor = new SecurityFilterChainPostProcessor

  "a security post processor" should "remove basic auth filters" in {
    val pre = new DefaultSecurityFilterChain(null, new BasicAuthenticationFilter, new DigestAuthenticationFilter)
    val post = processor.postProcessAfterInitialization(pre, null).asInstanceOf[DefaultSecurityFilterChain]
    assert(post.getFilters.size === 1)
    assert(post.getFilters.asScala.find(_.isInstanceOf[BasicAuthenticationFilter]).isEmpty)
  }
}
