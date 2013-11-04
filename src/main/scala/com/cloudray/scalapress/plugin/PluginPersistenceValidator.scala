package com.cloudray.scalapress.plugin

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.hibernate.SessionFactory
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import scala.collection.JavaConverters._
import org.hibernate.criterion.CriteriaSpecification
import com.cloudray.scalapress.framework.{Logging, ComponentClassScanner}

/** @author Stephen Samuel */
@Component
@Autowired
class SpringPluginPersistenceValidator(pluginPersistence: PluginPersistence) extends Logging {

  @PostConstruct
  def run(): Unit = {

    ComponentClassScanner.singleInstanceClasses.foreach(singleton => {
      try {
        logger.info("Checking for existing instance of {}", singleton)
        if (pluginPersistence.findAll(singleton).isEmpty) {
          logger.info("Creating single instance of {}", singleton)
          pluginPersistence.save(singleton.newInstance)
        }
      } catch {
        case e: Exception => logger.error(e.toString)
      }
    })
  }

}

trait PluginPersistence {
  def save(any: Any): Unit
  def findAll(`type`: Class[_]): Iterable[_]
}

@Component
@Autowired
@Transactional
class PluginPersistenceImpl(sf: SessionFactory) extends PluginPersistence {
  override def save(any: Any): Unit = sf.getCurrentSession.save(any)
  override def findAll(`type`: Class[_]): Iterable[_] = sf.getCurrentSession.createCriteria(`type`)
    .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
    .list.asScala
}