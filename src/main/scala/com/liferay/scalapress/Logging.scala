package com.liferay.scalapress

import org.slf4j.LoggerFactory
import javax.persistence.Transient
import reflect.BeanProperty

/* @author Stephen K Samuel */
trait Logging {

    @Transient
    @BeanProperty
    val logger = LoggerFactory.getLogger(getClass)
}
