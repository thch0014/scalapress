package com.liferay.scalapress.plugin.profile

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */
trait PasswordTokenDao extends GenericDao[PasswordToken, java.lang.Long] {
    def find(email: String, token: String): Option[PasswordToken]
}

@Component
@Transactional
class PasswordTokenDaoImpl extends GenericDaoImpl[PasswordToken, java.lang.Long] with PasswordTokenDao {
    def find(email: String, token: String): Option[PasswordToken] = None
}