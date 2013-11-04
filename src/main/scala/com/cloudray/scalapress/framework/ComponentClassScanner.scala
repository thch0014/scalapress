package com.cloudray.scalapress.framework

import org.springframework.util.ClassUtils
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import collection.mutable.ListBuffer
import scala.collection.JavaConversions._
import com.cloudray.scalapress.widgets.Widget
import com.cloudray.scalapress.section.Section
import java.lang.annotation.Annotation
import com.cloudray.scalapress.payments.{Callback, PaymentPlugin}
import com.cloudray.scalapress.plugin.SingleInstance
import java.lang.reflect.Modifier

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
    scanner.addIncludeFilter(new org.springframework.core.`type`.filter.AssignableTypeFilter(klass))
    scanner.getComponentClasses(BASE_PACKAGE).map(_.asInstanceOf[Class[T]])
  }

  def getAnnotatedClasses[_](annotationType: Class[_ <: Annotation]) = {
    val scanner = new ComponentClassScanner
    scanner.addIncludeFilter(new org.springframework.core.`type`.filter.AnnotationTypeFilter(annotationType))
    scanner.getComponentClasses(BASE_PACKAGE)
  }
}

object ComponentClassScanner {

  lazy val callbacks: Seq[Class[_]] = new ComponentClassScanner().getAnnotatedClasses(classOf[Callback])
  lazy val menus: Seq[Class[MenuProvider]] = new ComponentClassScanner().getSubtypes(classOf[MenuProvider])
  lazy val paymentPlugins: Seq[Class[PaymentPlugin]] = new ComponentClassScanner()
    .getSubtypes(classOf[PaymentPlugin])
    .filterNot(arg => Modifier.isAbstract(arg.getModifiers))
  lazy val sections: Seq[Class[Section]] = new ComponentClassScanner().getSubtypes(classOf[Section])
  lazy val singleInstanceClasses: Seq[Class[_]] =
    new ComponentClassScanner().getAnnotatedClasses(classOf[SingleInstance])
  lazy val widgets: Seq[Class[Widget]] = new ComponentClassScanner().getSubtypes(classOf[Widget])
  lazy val tags: Seq[Class[_]] = new ComponentClassScanner().getAnnotatedClasses(classOf[Tag])
}