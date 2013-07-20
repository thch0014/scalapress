package com.cloudray.scalapress

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.{Configuration, Bean}
import com.cloudray.scalapress.plugin.asset.amazons3.AmazonS3AssetStore

/** @author Stephen Samuel */
@Configuration
class ScalapressConfiguration {

  @Value("${s3.accessKey}") var accessKey: String = _
  @Value("${s3.secretKey}") var secretKey: String = _
  @Value("${s3.bucketName.images}") var bucketName: String = _
  @Value("${s3.cdn.images}") var cdn: String = _

  @Bean def assetStore = new AmazonS3AssetStore(cdn, secretKey, accessKey, bucketName)
}
