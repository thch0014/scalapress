package com.cloudray.scalapress.media

import com.cloudray.scalapress.framework.ScalapressContext

/**
 *
 * @author Stephen Samuel */
class ImageResolver(context: ScalapressContext) {

  def resolve(content: String) =
    content
      .replace("src=\"/images/", "src=\"" + context.assetStore.baseUrl + "/")
      .replace("src=\"images/", "src=\"" + context.assetStore.baseUrl + "/")
}
