package com.cloudray.scalapress.payments

import javax.persistence._
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import org.springframework.beans.factory.annotation.Autowired
import scala.beans.BeanProperty
import com.cloudray.scalapress.framework.ComponentClassScanner

/** @author Stephen Samuel */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class PaymentPlugin {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  @BeanProperty
  protected var id: Long = _

  def name: String
  def processor: PaymentProcessor
  def enabled: Boolean
}

trait PaymentPluginDao extends GenericDao[PaymentPlugin, java.lang.Long] {
  def enabled: Seq[PaymentPlugin]
}

@Component
@Transactional
class PaymentPluginDaoImpl extends GenericDaoImpl[PaymentPlugin, java.lang.Long] with PaymentPluginDao {
  def enabled: Seq[PaymentPlugin] = findAll(classOf[PaymentPlugin]).filter(p => p.enabled)
}

@Component
@Autowired
class PaymentPluginValidator(paymentPluginDao: PaymentPluginDao) {

  @PostConstruct
  def ensurePluginsCreated() {
    ComponentClassScanner.paymentPlugins.foreach(plugin => {
      val plugins = paymentPluginDao.findAll(classOf[PaymentPlugin])
      if (!plugins.exists(arg => arg.getClass.isAssignableFrom(plugin) || plugin.isAssignableFrom(arg.getClass)))
        paymentPluginDao.save(plugin.newInstance)
    })
  }
}

