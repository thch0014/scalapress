package com.liferay.scalapress.util

import org.springframework.util.ClassUtils
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import collection.mutable.ListBuffer
import scala.collection.JavaConversions._

/** @author Stephen Samuel */
class ComponentClassScanner extends ClassPathScanningCandidateComponentProvider(false) {

    def getComponentClasses(basePackage: String): List[Class[_]] = {
        val buffer = new ListBuffer[Class[_]]

        for (candidate <- findCandidateComponents(basePackage)) {
            try {
                val cls: Class[_] = ClassUtils
                  .resolveClassName(candidate.getBeanClassName, ClassUtils.getDefaultClassLoader)
                buffer.add(cls)
            }
            catch {
                case ex: Throwable => {
                    ex.printStackTrace()
                }
            }
        }

        buffer.toList
    }
}
