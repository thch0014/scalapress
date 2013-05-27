package com.liferay.scalapress.plugin.profile

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}
import com.googlecode.genericdao.search.Search
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct

/** @author Stephen Samuel */
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

trait PasswordTokenDao extends GenericDao[PasswordToken, java.lang.Long] {
    def find(email: String, token: String): Option[PasswordToken]
}

@Component
@Transactional
class PasswordTokenDaoImpl extends GenericDaoImpl[PasswordToken, java.lang.Long] with PasswordTokenDao {
    def find(email: String, token: String): Option[PasswordToken] = {
        val search = new Search(classOf[PasswordToken])
        search.addFilterEqual("email", email)
        search.addFilterEqual("token", token)
        Option(super.searchUnique(search))
    }
}