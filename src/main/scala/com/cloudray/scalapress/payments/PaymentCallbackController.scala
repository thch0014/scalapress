package com.cloudray.scalapress.payments

import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.Logging
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}
import org.springframework.stereotype.Controller

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("payment/callback"))
class PaymentCallbackController extends Logging {

  @Autowired var paymentCallbackService: PaymentCallbackService = _

  @ResponseBody
  @RequestMapping
  def callback(req: HttpServletRequest, resp: HttpServletResponse) {
    logger.info("Callback received: {}", req.getParameterMap)
    paymentCallbackService.callbacks(req)
    resp.setStatus(200)
  }

  @ResponseBody
  @RequestMapping(Array("listing"))
  @deprecated("for h4s, remove once their paypal updated")
  def callbackh4s(req: HttpServletRequest, resp: HttpServletResponse) {
    callback(req, resp)
  }
}
