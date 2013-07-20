package com.cloudray.scalapress.util

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Value
import javax.annotation.PostConstruct
import com.cloudray.scalapress.Logging

/** @author Stephen Samuel */
@Component
class UrlGeneratorBootstrap extends Logging {

  @Value("${url.strategy:None}") var strategyClassName: String = _

  @PostConstruct
  def setup() {
    try {
      UrlGenerator.strategy = Class.forName(strategyClassName).newInstance().asInstanceOf[UrlStrategy]
    } catch {
      case e: Exception => logger.debug("Could not load url generator class {}", strategyClassName)
    }
  }
}
