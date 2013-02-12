package com.liferay.scalapress.plugin.ecommerce

import javax.persistence.{EnumType, Enumerated, Column, JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Table, Entity}
import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import reflect.BeanProperty
import com.liferay.scalapress.domain.Markup
import com.liferay.scalapress.enums.CheckoutMethod

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_shopping")
class ShoppingPlugin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ManyToOne
    @JoinColumn(name = "basketMarkup")
    @BeanProperty var basketMarkup: Markup = _

    @Enumerated(EnumType.STRING)
    @BeanProperty var checkoutMethod: CheckoutMethod = CheckoutMethod.NO_ACCOUNTS

    @ManyToOne
    @JoinColumn(name = "basketLineMarkup")
    @BeanProperty var basketLineMarkup: Markup = _

    @Column(length = 100000)
    @BeanProperty var terms: String = _
    @BeanProperty var termsAcceptance: Boolean = _

    @Column(name = "checkoutScripts")
    @BeanProperty var checkoutConfirmationScripts: String = _
    @BeanProperty var checkoutConfirmationText: String = _
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
class ShoppingPluginValidator {
    @Autowired var dao: ShoppingPluginDao = _
    @PostConstruct def ensureOne() {
        if (dao.findAll().size == 0) {
            val plugin = new ShoppingPlugin
            dao.save(plugin)
        }
    }
}