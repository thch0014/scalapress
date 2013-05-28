package com.cloudray.scalapress.plugin.staticcache

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/** @author Stephen Samuel */
@Component
class WriteoutCacheProperties {

    @Value("${plugin.writeoutcache.dir:/WEB-INF/cache}") var directory: String = _
    @Value("${plugin.writeoutcache.enabled:false}") var enabled: Boolean = _
    @Value("${plugin.writeoutcache.timeout:300}") var timeout: Long = _
}
