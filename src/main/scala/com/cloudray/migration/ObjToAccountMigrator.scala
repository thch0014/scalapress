package com.cloudray.migration

import com.cloudray.scalapress.obj.{TypeDao, ObjectQuery, ObjectDao}
import com.cloudray.scalapress.account.{AccountType, AccountTypeDao, AccountDao, Account}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.cloudray.scalapress.Logging
import javax.annotation.PostConstruct

/** @author Stephen Samuel */
@Component
@Autowired
class ObjToAccountMigrator(accountDao: AccountDao,
                           objectDao: ObjectDao,
                           typeDao: TypeDao,
                           accountTypeDao: AccountTypeDao) extends Logging {

  @PostConstruct
  def run() {

    val accountType = accountTypeDao.findAll().headOption match {
      case Some(t) => t
      case None =>
        val accountType = new AccountType
        accountType.name = "Account"
        accountTypeDao.save(accountType)
        accountType
    }

    typeDao.getAccount.foreach(objectType => {
      val query = new ObjectQuery().withTypeId(objectType.id).withMaxResults(500)
      migrate()

      def migrate() {
        val results = objectDao.search(query).results
        logger.info("Migrating {} objects into accounts", results.size)

        results.foreach(obj => {
          val account = new Account
          account.id = obj.id
          account.passwordHash = obj.passwordHash
          account.dateCreated = obj.dateCreated
          account.dateUpdated = obj.dateUpdated
          account.status = Account.STATUS_ACTIVE
          account.email = obj.email_deprecated
          account.name = obj.name
          account.registrationIpAddress = obj.ipAddress
          account.accountType = accountType

          accountDao.save(account)
          objectDao.remove(obj)
        })
        if (results.size > 0)
          migrate()
      }
    })
  }
}
