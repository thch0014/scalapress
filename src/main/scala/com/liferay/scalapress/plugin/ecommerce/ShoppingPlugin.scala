package com.liferay.scalapress.plugin.ecommerce

import javax.persistence.{JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Table, Entity}
import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import reflect.BeanProperty
import com.liferay.scalapress.domain.Markup

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_shopping")
class ShoppingPlugin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ManyToOne
    @JoinColumn(name = "markup")
    @BeanProperty var basketMarkup: Markup = _
}

trait ShoppingPluginDao extends GenericDao[ShoppingPlugin, java.lang.Long] {
    def get: ShoppingPlugin
}

@Component
@Transactional
class ShoppingPluginDaoImpl extends GenericDaoImpl[ShoppingPlugin, java.lang.Long] with ShoppingPluginDao {
    def get = findAll.head
}

@Component
class SearchPluginDaoValidator {
    @Autowired var dao: ShoppingPluginDao = _
    @PostConstruct def ensureOne() {
        if (dao.findAll().size == 0) {
            val plugin = new ShoppingPlugin
            dao.save(plugin)
        }
    }
}