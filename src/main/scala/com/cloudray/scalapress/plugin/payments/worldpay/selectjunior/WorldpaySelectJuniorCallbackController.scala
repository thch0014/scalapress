package com.cloudray.scalapress.plugin.payments.worldpay.selectjunior

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import scala.Array
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("/payment/worldpay/selectjunior"))
class WorldpaySelectJuniorCallbackController {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(Array("callback"))
    @ResponseBody
    def edit(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.setStatus(200)
    }
}
