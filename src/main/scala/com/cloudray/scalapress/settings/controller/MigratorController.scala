package com.cloudray.scalapress.settings.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, PathVariable, RequestMapping}
import com.cloudray.scalapress.{ScalapressContext, Logging}
import com.cloudray.migration.ecreator.ECImageMigrator
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("migration"))
class MigratorController extends Logging {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(Array("images/{domain}"))
    @ResponseBody
    def images(@PathVariable("domain") domain: String): String = {
        logger.info("Invoking image migration [{}]", domain)
        val migrator = new ECImageMigrator
        migrator.migrate(domain)
        "Migration will commence post haste"
    }
}
