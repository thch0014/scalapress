package com.liferay.scalapress.plugin.payments.sagepayform

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.stereotype.Controller

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("payment-callback-sagepayform"))
class SagepayFormCallbackController {

    def success = ""
    def failure = ""
}
