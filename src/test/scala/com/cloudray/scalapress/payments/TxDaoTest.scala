package com.cloudray.scalapress.payments

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext
import com.cloudray.scalapress.payments.Transaction

/** @author Stephen Samuel */
class TxDaoTest extends FunSuite with MockitoSugar {

  test("persisting an tx can be retrieved by id and sets fields") {

    val tx = new Transaction
    tx.transactionId = "tx56NBV"
    tx.status = "completed"
    tx.date = 112256783566l
    tx.authCode = "authy1"

    assert(tx.id == 0)
    TestDatabaseContext.txDao.save(tx)
    assert(tx.id > 0)

    val t2 = TestDatabaseContext.txDao.find(tx.id)
    assert("tx56NBV" === t2.transactionId)
    assert("completed" === t2.status)
    assert("authy1" === t2.authCode)
    assert(112256783566l === t2.date)
  }
}
