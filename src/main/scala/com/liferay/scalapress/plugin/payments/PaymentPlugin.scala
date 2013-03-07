package com.liferay.scalapress.plugin.payments

import reflect.BeanProperty
import javax.persistence.{InheritanceType, Inheritance, Entity, GenerationType, GeneratedValue, Id}
import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class PaymentPlugin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var enabled: Boolean = _

    def name: String

    def processor: FormPaymentProcessor
}

trait PaymentPluginDao extends GenericDao[PaymentPlugin, java.lang.Long] {
    def enabled: Seq[PaymentPlugin]
}

@Component
@Transactional
class PaymentPluginDaoImpl extends GenericDaoImpl[PaymentPlugin, java.lang.Long] with PaymentPluginDao {
    def enabled: Seq[PaymentPlugin] = findAll.filter(p => p.enabled)
}