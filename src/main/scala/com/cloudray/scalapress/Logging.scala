package com.cloudray.scalapress

import org.slf4j.LoggerFactory
import javax.persistence.Transient

/* @author Stephen K Samuel */
trait Logging {
  @Transient
  val logger = LoggerFactory.getLogger(getClass)
}
