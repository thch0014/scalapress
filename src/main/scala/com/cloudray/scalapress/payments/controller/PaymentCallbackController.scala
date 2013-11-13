package com.cloudray.scalapress.payments.controller

import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}
import org.springframework.stereotype.Controller
import com.cloudray.scalapress.framework.Logging
import com.cloudray.scalapress.payments.PaymentCallbackService

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("payment/callback"))
class PaymentCallbackController(paymentCallbackService: PaymentCallbackService) extends Logging {

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
