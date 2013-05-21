package com.liferay.scalapress.util

import org.springframework.util.ClassUtils
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import collection.mutable.ListBuffer
import scala.collection.JavaConversions._
import org.springframework.core.`type`.filter.{AnnotationTypeFilter, AssignableTypeFilter}
import org.elasticsearch.plugins.Plugin
import com.liferay.scalapress.widgets.Widget
import com.liferay.scalapress.section.Section
import com.liferay.scalapress.Tag
import com.liferay.scalapress.plugin.payments.PaymentPlugin
import java.lang.annotation.Annotation
import com.liferay.scalapress.settings.lifecycle.MenuItem

/** @author Stephen Samuel */
class ComponentClassScanner extends ClassPathScanningCandidateComponentProvider(false) {

    def getComponentClasses(basePackage: String): List[Class[_]] = {
        val buffer = new ListBuffer[Class[_]]

        for ( candidate <- findCandidateComponents(basePackage) ) {
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

    def getSubtypes[T](klass: Class[T]) = {
        val scanner = new ComponentClassScanner
        scanner.addIncludeFilter(new AssignableTypeFilter(klass))
        scanner.getComponentClasses("com.liferay.scalapress").map(_.asInstanceOf[Class[T]])
    }

    def getAnnotatedClasses[T <: Annotation](annotationType: Class[T]) = {
        val scanner = new ComponentClassScanner
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotationType))
        scanner.getComponentClasses("com.liferay.scalapress").map(_.asInstanceOf[Class[T]])
    }
}

object ComponentClassScanner {

    def menus: Seq[Class[MenuItem]] = new ComponentClassScanner().getSubtypes(classOf[MenuItem])
    def paymentPlugins: Seq[Class[PaymentPlugin]] = new ComponentClassScanner().getSubtypes(classOf[PaymentPlugin])
    def sections: Seq[Class[Section]] = new ComponentClassScanner().getSubtypes(classOf[Section])
    def plugins: Seq[Class[Plugin]] = new ComponentClassScanner().getSubtypes(classOf[Plugin])
    def widgets: Seq[Class[Widget]] = new ComponentClassScanner().getSubtypes(classOf[Widget])
    def tags: Seq[Class[Tag]] = new ComponentClassScanner().getAnnotatedClasses(classOf[Tag])
}