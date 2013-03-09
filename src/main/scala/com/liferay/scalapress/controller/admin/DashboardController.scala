package com.liferay.scalapress.controller.admin

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice"))
class DashboardController {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def dashboard = "admin/dashboard.vm"
}
