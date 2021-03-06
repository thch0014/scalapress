package com.cloudray.scalapress.plugin

import org.springframework.beans.factory.annotation.Value
import javax.annotation.PostConstruct
import org.springframework.stereotype.Component
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import com.cloudray.scalapress.framework.Logging

/** @author Stephen Samuel */
@Component
class PluginInstantiator extends Logging with ApplicationContextAware {

    var applicationContext: ApplicationContext = _
    @Value("${plugins:none}") var plugins: Array[String] = _

    @PostConstruct
    def createPlugins() {
        logger.info("Instaniating plugins: {}", plugins)

        plugins.filterNot(_ == "none").foreach(plugin => {
            try {
                val klass = Class.forName(plugin)
                val bean = applicationContext
                  .getAutowireCapableBeanFactory
                  .createBean(klass, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
                logger.debug("Created plugin [{}]", bean)
            } catch {
                case e: Exception => logger.error("{}", e)
            }
        })
    }

    def setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }
}
