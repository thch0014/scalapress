package com.liferay.scalapress

import dao.ObjectDao
import org.springframework.beans.factory.annotation.{Autowired, Value}
import service.asset.AmazonS3AssetStore
import org.springframework.context.annotation.{Configuration, Bean}
import service.search.ElasticSearchService

/** @author Stephen Samuel */
@Configuration
class ScalaPressConfiguration {

    @Value("${s3.accessKey}") var accessKey: String = _
    @Value("${s3.secretKey}") var secretKey: String = _
    @Value("${s3.bucketName.images}") var bucketName: String = _
    @Value("${s3.cdn.images}") var cdn: String = _

    @Autowired var objectDao: ObjectDao = _

    @Bean def assetStore = new AmazonS3AssetStore(cdn, secretKey, accessKey, bucketName)
    @Bean def elasticsearch = new ElasticSearchService(objectDao)
}
