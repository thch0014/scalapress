package com.liferay.scalapress.service.theme

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import com.liferay.scalapress.service.StorageProvider

@Service
class ThemeStorageProvider extends StorageProvider {

    /**
     * The base url of the external CDN
     */
    @Value("${s3.cdn.theme}")
    var cdnUrl: String = _

    @Value("${s3.bucketName.theme}")
    var bucketName: String = _
}