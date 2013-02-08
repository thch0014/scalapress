package com.liferay.scalapress.plugin.payments.sagepayform

import javax.persistence.{Entity, Table, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty
import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_sagepay")
class SagepayFormPlugin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var sagePayVendorName: String = _
    @BeanProperty var sagePayEncryptionPassword: String = _
    @BeanProperty var sagePayVendorEmail: String = _
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
