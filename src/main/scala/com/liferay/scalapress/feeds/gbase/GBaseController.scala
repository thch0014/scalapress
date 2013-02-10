package com.liferay.scalapress.feeds.gbase

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@RequestMapping(value = Array("backoffice/gbase"))
class GBaseController {

    @Autowired var gbase: GoogleBaseTask = _

    @RequestMapping
    def view = "admin/feeds/gbase/view.vm"

    @RequestMapping(value = Array("run"))
    def run = {
        gbase.run()
        view
    }
}
