package com.liferay.scalapress.service.theme.tag

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest, Logging}

/** @author Stephen Samuel */
object AssetTag extends ScalapressTag with TagBuilder with Logging {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        params.get("url") match {
            case None => None
            case Some(url) => Option(context.assetStore.link(url))
        }
    }
}
