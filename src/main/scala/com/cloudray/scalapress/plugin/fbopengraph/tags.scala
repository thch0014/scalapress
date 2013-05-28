package com.cloudray.scalapress.plugin.fbopengraph

import com.cloudray.scalapress.theme.tag.ScalapressTag
import com.cloudray.scalapress.{Tag, ScalapressRequest}
import com.cloudray.scalapress.plugin.friendlyurl.FriendlyUrlGenerator
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Tag("og_title")
class OpenGraphTitleTag extends ScalapressTag {

    def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
        request.obj.map(obj => <meta property="og:title" content={obj.name}/>.toString())
    }
}

@Tag("og_url")
class OpenGraphUrlTag extends ScalapressTag {

    def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
        request.obj.map(obj => <meta property="og:url" content={FriendlyUrlGenerator.friendlyUrl(obj)}/>.toString())
    }
}

@Tag("og_site")
class OpenGraphSiteTag extends ScalapressTag {

    def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
        val installation = request.context.installationDao.get
        Some(<meta property="og:site_name" content={installation.name}/>.toString())
    }
}

@Tag("og_image")
class OpenGraphImageTag extends ScalapressTag {

    def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
        request.obj
          .filter(_.images.size > 0)
          .map(obj => {
            val images = obj.images.asScala.toSeq.sortBy(_.id)
            val imageLink = request.context.assetStore.link(images(0).filename)
                <meta property="og:image" content={imageLink}/>.toString()
        })
    }
}

