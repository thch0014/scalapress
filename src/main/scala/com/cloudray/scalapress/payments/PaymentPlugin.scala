package com.cloudray.scalapress.payments

import javax.persistence.{InheritanceType, Inheritance, Entity, GenerationType, GeneratedValue, Id}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao, ComponentClassScanner}
import org.springframework.beans.factory.annotation.Autowired
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class PaymentPlugin {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  @BeanProperty var id: Long = _

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
  def enabled: Seq[PaymentPlugin] = findAll.filter(p => p.enabled)
}

@Component
class PaymentPluginValidator {

  @Autowired var paymentPluginDao: PaymentPluginDao = _

  @PostConstruct
  def ensurePluginsCreated() {
    ComponentClassScanner.paymentPlugins.foreach(plugin => {
      val plugins = paymentPluginDao.findAll()
      if (!plugins.exists(arg => arg.getClass.isAssignableFrom(plugin) || plugin.isAssignableFrom(arg.getClass)))
        paymentPluginDao.save(plugin.newInstance)

    })
  }
}

