package com.cloudray.scalapress.payments

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import org.springframework.ui.ModelMap
import org.mockito.{Matchers, Mockito}
import com.googlecode.genericdao.search.Search
import scala.collection.JavaConverters._
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.payments.controller.TransactionListController
import com.cloudray.scalapress.payments.Transaction

/** @author Stephen Samuel */
class TransactionListControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val dao = mock[TransactionDao]
  val controller = new TransactionListController(dao)
  val model = new ModelMap

  val tx = new Transaction
  tx.id = 9
  tx.amount = 123

  val req = mock[HttpServletRequest]

  val list = List(tx)
  Mockito.when(dao.search(Matchers.any[Search])).thenReturn(list)

  "a tx view controller" should "lookup tx by search" in {
    controller.search(1, model, req)
    assert(model.get("results") === list.asJava)
  }
}
