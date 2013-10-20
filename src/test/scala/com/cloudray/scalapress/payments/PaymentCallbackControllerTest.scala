package com.cloudray.scalapress.payments

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.mockito.Mockito

/** @author Stephen Samuel */
class PaymentCallbackControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val paymentCallbackService = mock[PaymentCallbackService]
  val controller = new PaymentCallbackController(paymentCallbackService)
  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]

  test("that the controller invokes callbacks from parameters") {
    controller.callback(req, resp)
    Mockito.verify(paymentCallbackService).callbacks(req)
  }
}
