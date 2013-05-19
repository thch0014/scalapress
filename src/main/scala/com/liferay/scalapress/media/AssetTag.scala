package com.liferay.scalapress.media

import com.liferay.scalapress.{Tag, ScalapressContext, ScalapressRequest, Logging}
import com.liferay.scalapress.theme.tag.{TagBuilder, ScalapressTag}

/** @author Stephen Samuel */
@Tag("asset")
class AssetTag extends ScalapressTag with TagBuilder with Logging {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        params.get("url") match {
            case None => None
            case Some(url) => Option(context.assetStore.link(url))
        }
    }
}
