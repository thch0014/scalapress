package com.cloudray.scalapress.media

import com.cloudray.scalapress.{Tag, ScalapressRequest, Logging}
import com.cloudray.scalapress.theme.tag.{TagBuilder, ScalapressTag}

/** @author Stephen Samuel */
@Tag("asset")
class AssetTag extends ScalapressTag with TagBuilder with Logging {
    def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
        params.get("url") match {
            case None => None
            case Some(url) => Option(request.context.assetStore.link(url))
        }
    }
}
