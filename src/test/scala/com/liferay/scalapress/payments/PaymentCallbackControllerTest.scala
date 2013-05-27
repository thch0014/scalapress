package com.liferay.scalapress.payments

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.mockito.Mockito

/** @author Stephen Samuel */
class PaymentCallbackControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val controller = new PaymentCallbackController
    controller.paymentCallbackService = mock[PaymentCallbackService]
    val req = mock[HttpServletRequest]
    val resp = mock[HttpServletResponse]

    test("that the controller invokes callbacks from parameters") {
        controller.callback(req, resp)
        Mockito.verify(controller.paymentCallbackService).callbacks(req)
    }
}
