package com.liferay.scalapress.plugin.payments.sagepayform

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.dao.settings.InstallationDao
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("payment/sagepayform"))
class PaymentController {

    @Autowired var context: ScalapressContext = _
    @Autowired var dao: SagepayFormPluginDao = _
    @Autowired var installationDao: InstallationDao = _

    @ResponseBody
    @RequestMapping
    def params(req: HttpServletRequest) = {
        val sreq = ScalapressRequest(req, context)
        SagepayFormService.params(sreq.basket, dao.get, installationDao.get.domain)
    }
}
