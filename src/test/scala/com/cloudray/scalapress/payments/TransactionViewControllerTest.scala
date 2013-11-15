package com.cloudray.scalapress.payments

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import org.springframework.ui.ModelMap
import org.mockito.Mockito
import com.cloudray.scalapress.payments.controller.TransactionViewController

/** @author Stephen Samuel */
class TransactionViewControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val dao = mock[TransactionDao]
  val controller = new TransactionViewController(dao)
  val model = new ModelMap

  val tx = new Transaction
  tx.id = 9
  tx.amount = 123

  Mockito.when(dao.find(9)).thenReturn(tx)

  "a tx view controller" should "lookup tx by id" in {
    controller.view(9, model)
    assert(model.get("tx") === tx)
  }
}
