package com.liferay.scalapress.plugin.ecommerce.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, ResponseBody, RequestMapping}
import scala.Array
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.plugin.ecommerce.ShoppingPluginDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("checkout/terms"))
class TermsController {

    @Autowired var shoppingPluginDao: ShoppingPluginDao = _

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def showAddress: String = {
        shoppingPluginDao.get.terms
    }
}
