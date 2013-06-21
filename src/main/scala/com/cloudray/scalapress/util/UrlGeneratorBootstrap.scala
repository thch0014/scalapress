package com.cloudray.scalapress.util

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Value
import javax.annotation.PostConstruct
import com.cloudray.scalapress.Logging

/** @author Stephen Samuel */
@Component
class UrlGeneratorBootstrap extends Logging {

    @Value("${url.strategy:None}") var strategy: String = _

    @PostConstruct
    def setup() {
        try {
            if (strategy != "None")
                UrlGenerator.strategy = Class.forName(strategy).newInstance().asInstanceOf[UrlStrategy]
        } catch {
            case e: Exception => logger.warn("{}", e)
        }
    }
}
