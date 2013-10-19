package com.cloudray.migration

import com.cloudray.scalapress.obj.ObjectDao
import com.cloudray.scalapress.account.{AccountType, AccountTypeDao, AccountDao, Account}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.cloudray.scalapress.Logging

/** @author Stephen Samuel */
@Component
@Autowired
class ObjToAccountMigrator(accountDao: AccountDao, objectDao: ObjectDao, accountTypeDao: AccountTypeDao)
  extends Logging {

  def run() {

    val accountType = accountTypeDao.findAll().headOption match {
      case Some(t) => t
      case None =>
        val accountType = new AccountType
        accountType.name = "Basic Account"
        accountTypeDao.save(accountType)
        accountType
    }

    objectDao.findAll().filter(_.objectType.name.toLowerCase.startsWith("account")).foreach(obj => {
      val account = new Account
      account.id = obj.id
      account.passwordHash = obj.passwordHash
      account.dateCreated = obj.dateCreated
      account.status = Account.STATUS_ACTIVE
      account.email = obj.email
      account.name = obj.name
      account.registrationIpAddress = obj.ipAddress
      account.accountType = accountType
      logger.debug("Creating account [{}]", account)
      accountDao.save(account)
      logger.debug("Removing account-object [{}]", obj)
      objectDao.remove(obj)
    })
  }
}
