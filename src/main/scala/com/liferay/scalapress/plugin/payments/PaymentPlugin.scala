package com.liferay.scalapress.plugin.payments

import reflect.BeanProperty
import javax.persistence.{InheritanceType, Inheritance, Entity, GenerationType, GeneratedValue, Id}
import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import com.liferay.scalapress.util.ComponentClassScanner
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class PaymentPlugin {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @BeanProperty var id: Long = _

    def name: String
    def processor: FormPaymentProcessor
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

