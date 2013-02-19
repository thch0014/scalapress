package com.liferay.scalapress.util

import org.springframework.util.ClassUtils
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import collection.mutable.ListBuffer
import scala.collection.JavaConversions._
import org.springframework.core.`type`.filter.AssignableTypeFilter
import com.liferay.scalapress.Section
import org.elasticsearch.plugins.Plugin
import com.liferay.scalapress.widgets.Widget

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

object ComponentClassScanner {
    def sections = {
        val scanner = new ComponentClassScanner
        scanner.addIncludeFilter(new AssignableTypeFilter(classOf[Section]))
        scanner.getComponentClasses("com.liferay.scalapress")
    }
    def plugins = {
        val scanner = new ComponentClassScanner
        scanner.addIncludeFilter(new AssignableTypeFilter(classOf[Plugin]))
        scanner.getComponentClasses("com.liferay.scalapress")
    }
    def widgets = {
        val scanner = new ComponentClassScanner
        scanner.addIncludeFilter(new AssignableTypeFilter(classOf[Widget]))
        scanner.getComponentClasses("com.liferay.scalapress")
    }
}