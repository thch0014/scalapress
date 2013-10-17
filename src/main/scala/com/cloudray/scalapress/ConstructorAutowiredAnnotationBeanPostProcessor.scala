package com.cloudray.scalapress

import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter
import com.cloudray.scalapress.Logging
import org.springframework.stereotype.Component
import java.lang.reflect.Constructor
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Component
class ConstructorAutowiredAnnotationBeanPostProcessor
  extends InstantiationAwareBeanPostProcessorAdapter
  with org.springframework.core.Ordered
  with Logging {

  logger.error("Configured for Scala constructor support")

  override def determineCandidateConstructors(beanClass: Class[_], beanName: String): Array[Constructor[_]] = {
    if (isAutowiredClass(beanClass) && beanClass.getDeclaredConstructors.size == 1) {
      Array(beanClass.getDeclaredConstructors()(0))
    } else {
      null
    }
  }

  def isAutowiredClass(beanClass: Class[_]) = beanClass.isAnnotationPresent(classOf[Autowired])
  def getOrder: Int = Integer.MAX_VALUE
}