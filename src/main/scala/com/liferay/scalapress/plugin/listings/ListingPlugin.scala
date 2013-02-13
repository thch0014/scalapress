package com.liferay.scalapress.plugin.listings

import javax.persistence.{GenerationType, GeneratedValue, Id, Table, Entity}
import reflect.BeanProperty
import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_listings")
class ListingsPlugin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _
}

trait ListingsPluginDao extends GenericDao[ListingsPlugin, java.lang.Long] {
    def get: ListingsPlugin
}

@Component
@Transactional
class ListingsPluginDaoImpl extends GenericDaoImpl[ListingsPlugin, java.lang.Long] with ListingsPluginDao {
    def get = findAll.head
}

@Component
class ListingsPluginDaoValidator {
    @Autowired var dao: ListingsPluginDao = _
    @PostConstruct def ensureOne() {
        if (dao.findAll().size == 0) {
            val plugin = new ListingsPlugin
            dao.save(plugin)
        }
    }
}