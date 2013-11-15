package com.cloudray.migration

import com.cloudray.scalapress.item.{TypeDao, ItemQuery, ItemDao}
import com.cloudray.scalapress.account.{AccountType, AccountTypeDao, AccountDao, Account}
import org.springframework.beans.factory.annotation.Autowired
import javax.annotation.PostConstruct
import com.cloudray.scalapress.framework.Logging

/** @author Stephen Samuel */
//@Component
@Autowired
class ItemToAccountMigrator(accountDao: AccountDao,
                            objectDao: ItemDao,
                            typeDao: TypeDao,
                            accountTypeDao: AccountTypeDao) extends Logging {

  @PostConstruct
  def run() {

    val accountType = accountTypeDao.findAll.headOption match {
      case Some(t) => t
      case None =>
        val accountType = new AccountType
        accountType.name = "Account"
        accountTypeDao.save(accountType)
        accountType
    }

    typeDao.getAccount.foreach(objectType => {
      val query = new ItemQuery().withTypeId(objectType.id).withPageSize(200)
      migrate()

      def migrate() {
        val objects = objectDao.search(query).results
        logger.info("Migrating {} objects into accounts", objects.size)

        objects.foreach(obj => {
          val account = new Account
          account.id = obj.id
          //account.passwordHash = obj.password_deprecated
          account.dateCreated = obj.dateCreated
          account.dateUpdated = obj.dateUpdated
          account.status = Account.STATUS_ACTIVE
          //account.email = obj.email_deprecated
          account.name = obj.name
          account.accountType = accountType
          try {
            accountDao.save(account)
          } catch {
            case e: Exception => logger.error(e.toString)
          }
          try {
            objectDao.remove(obj)
          } catch {
            case e: Exception => logger.error(e.toString)
          }
        })
        if (objects.size > 0)
          migrate()
      }
    })
  }
}
