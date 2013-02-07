package com.liferay.scalapress.plugin.account

import com.liferay.scalapress.dao.{GenericDaoImpl, GenericDao}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import javax.persistence._
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_account")
class AccountPlugin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ElementCollection
    @BeanProperty var accounts: java.util.List[java.lang.Long] = new java.util.ArrayList[java.lang.Long]()
}

trait AccountPluginDao extends GenericDao[AccountPlugin, java.lang.Long] {
    def get: AccountPlugin
}

@Component
@Transactional
class AccountPluginDaoImpl extends GenericDaoImpl[AccountPlugin, java.lang.Long] with AccountPluginDao {
    def get = findAll.head
}

@Component
class AccountPluginValidator {
    @Autowired var dao: AccountPluginDao = _
    @PostConstruct def ensureOne() {
        if (dao.findAll().size == 0) {
            val plugin = new AccountPlugin
            dao.save(plugin)
        }
    }
}