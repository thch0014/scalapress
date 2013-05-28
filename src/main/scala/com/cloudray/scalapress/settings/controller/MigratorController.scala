package com.cloudray.scalapress.settings.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, PathVariable, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.Logging
import com.cloudray.scalapress.media.admin.ECImageMigrator

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("migration"))
class MigratorController extends Logging {

    @Autowired var migrator: ECImageMigrator = _

    @RequestMapping(Array("images/{domain}"))
    @ResponseBody
    def images(@PathVariable("domain") domain: String): String = {
        logger.info("Invoking image migration [{}]", domain)
        migrator.migrate(domain)
        "Migration will commence post haste"
    }
}
