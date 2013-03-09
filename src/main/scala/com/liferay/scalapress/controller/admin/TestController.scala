package com.liferay.scalapress.controller.admin

import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import org.springframework.stereotype.Controller

/** @author Stephen Samuel */
@Controller
class TestController {

    @Autowired var context: ScalapressContext = _

    @ResponseBody
    @RequestMapping(Array("test"))
    def test: String = context.paymentPluginDao.findAll().toString()
}
