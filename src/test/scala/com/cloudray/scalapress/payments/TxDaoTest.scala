package com.cloudray.scalapress.payments

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class TxDaoTest extends FunSuite with MockitoSugar {

  test("persisting an tx can be retrieved by id and sets fields") {

    val tx = new Transaction
    tx.transactionId = "tx56NBV"
    tx.status = "completed"
    tx.details = "super payment by visa"
    tx.date = 112256783566l

    assert(tx.id == 0)
    TestDatabaseContext.txDao.save(tx)
    assert(tx.id > 0)

    val t2 = TestDatabaseContext.txDao.find(tx.id)
    assert("tx56NBV" === t2.transactionId)
    assert("completed" === t2.status)
    assert("super payment by visa" === t2.details)
    assert(112256783566l === t2.date)
  }
}
