package com.cloudray.scalapress.payments

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.beans.factory.config.AutowireCapableBeanFactory

/** @author Stephen Samuel */
class TxDaoTest extends FunSuite with MockitoSugar {

    val context = new ClassPathXmlApplicationContext("/spring-db-test.xml")

    val dao = context
      .getAutowireCapableBeanFactory
      .createBean(classOf[TransactionDaoImpl], AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true)
      .asInstanceOf[TransactionDao]

    test("persisting an tx can be retrieved by id and sets fields") {

        val tx = new Transaction
        tx.transactionId = "tx56NBV"
        tx.status = "completed"
        tx.details = "super payment by visa"
        tx.date = 112256783566l

        assert(tx.id == 0)
        dao.save(tx)
        assert(tx.id > 0)

        val t2 = dao.find(tx.id)
        assert("tx56NBV" === tx.transactionId)
        assert("completed" === tx.status)
        assert("super payment by visa" === tx.details)
        assert(112256783566l === tx.date)
    }
}
