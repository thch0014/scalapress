package com.liferay.scalapress.plugin.payments.sagepayform

import javax.persistence.{Entity, Table}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import com.liferay.scalapress.plugin.payments.{PaymentProcessor, PaymentPlugin}
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_sagepay")
class SagepayFormPlugin extends PaymentPlugin {

    @BeanProperty var sagePayVendorName: String = _
    @BeanProperty var sagePayEncryptionPassword: String = _
    @BeanProperty var sagePayVendorEmail: String = _

    def name: String = "Sagepay"
    def processor: PaymentProcessor = new SagepayFormProcessor(this)
    def enabled = Option(sagePayVendorName).filter(_.trim.length > 0).isDefined
}

trait SagepayFormPluginDao extends GenericDao[SagepayFormPlugin, java.lang.Long] {
    def get: SagepayFormPlugin
}

@Component
@Transactional
class SagepayFormPluginDaoImpl extends GenericDaoImpl[SagepayFormPlugin, java.lang.Long] with SagepayFormPluginDao {
    def get = findAll.head
}

@Component
class SagepayFormPluginDaoImplValidator {
    @Autowired var dao: SagepayFormPluginDao = _
    @PostConstruct def ensureOne() {
        if (dao.findAll().size == 0) {
            val plugin = new SagepayFormPlugin
            dao.save(plugin)
        }
    }
}
