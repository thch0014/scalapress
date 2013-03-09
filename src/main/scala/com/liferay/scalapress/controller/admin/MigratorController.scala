package com.liferay.scalapress.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, PathVariable, RequestMapping}
import org.springframework.beans.factory.annotation.Value
import com.liferay.scalapress.service.asset.AmazonS3AssetStore
import com.liferay.scalapress.Logging

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("migration"))
class MigratorController extends Logging {

    @Value("${s3.accessKey}") var accessKey: String = _
    @Value("${s3.secretKey}") var secretKey: String = _
    @Value("${s3.bucketName.images}") var bucketName: String = _
    @Value("${s3.cdn.images}") var cdn: String = _

    @RequestMapping(Array("images/{domain}"))
    @ResponseBody
    def images(@PathVariable("domain") domain: String): String = {
        logger.info("Invoking image migration [{}]", domain)
        val s3 = new AmazonS3AssetStore(cdn, secretKey, accessKey, bucketName)
        //   EcreatorImageMigrator.migrate(domain, s3)
        "Migration will commence post haste"
    }
}
