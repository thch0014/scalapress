package com.liferay.scalapress.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, PathVariable, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.Logging
import com.liferay.scalapress.service.image.EcreatorImageMigrator

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("migration"))
class MigratorController extends Logging {

    @Autowired var migrator: EcreatorImageMigrator = _

    @RequestMapping(Array("images/{domain}"))
    @ResponseBody
    def images(@PathVariable("domain") domain: String): String = {
        logger.info("Invoking image migration [{}]", domain)
        migrator.migrate(domain)
        "Migration will commence post haste"
    }
}
