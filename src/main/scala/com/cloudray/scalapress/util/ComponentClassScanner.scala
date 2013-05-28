package com.cloudray.scalapress.util

import org.springframework.util.ClassUtils
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import collection.mutable.ListBuffer
import scala.collection.JavaConversions._
import org.springframework.core.`type`.filter.{AnnotationTypeFilter, AssignableTypeFilter}
import org.elasticsearch.plugins.Plugin
import com.cloudray.scalapress.widgets.Widget
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.{Callback, Tag}
import com.cloudray.scalapress.settings.lifecycle.MenuItem
import java.lang.annotation.Annotation
import com.cloudray.scalapress.payments.PaymentPlugin

/** @author Stephen Samuel */
class ComponentClassScanner extends ClassPathScanningCandidateComponentProvider(false) {

    val BASE_PACKAGE = "com.cloudray.scalapress"

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
        scanner.getComponentClasses(BASE_PACKAGE).map(_.asInstanceOf[Class[T]])
    }

    def getAnnotatedClasses[_](annotationType: Class[_ <: Annotation]) = {
        val scanner = new ComponentClassScanner
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotationType))
        scanner.getComponentClasses(BASE_PACKAGE)
    }
}

object ComponentClassScanner {

    lazy val callbacks: Seq[Class[_]] = new ComponentClassScanner().getAnnotatedClasses(classOf[Callback])
    lazy val menus: Seq[Class[MenuItem]] = new ComponentClassScanner().getSubtypes(classOf[MenuItem])
    lazy val paymentPlugins: Seq[Class[PaymentPlugin]] = new ComponentClassScanner().getSubtypes(classOf[PaymentPlugin])
    lazy val sections: Seq[Class[Section]] = new ComponentClassScanner().getSubtypes(classOf[Section])
    lazy val plugins: Seq[Class[Plugin]] = new ComponentClassScanner().getSubtypes(classOf[Plugin])
    lazy val widgets: Seq[Class[Widget]] = new ComponentClassScanner().getSubtypes(classOf[Widget])
    lazy val tags: Seq[Class[_]] = new ComponentClassScanner().getAnnotatedClasses(classOf[Tag])
}