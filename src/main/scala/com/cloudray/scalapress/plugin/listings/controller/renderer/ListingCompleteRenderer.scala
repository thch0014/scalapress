package com.cloudray.scalapress.plugin.listings.controller.renderer

import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.obj.Item
import com.cloudray.scalapress.util.{Scalate, UrlGenerator}

/** @author Stephen Samuel */
object ListingCompleteRenderer {

  def render(context: ScalapressContext, listing: Item) = {
    val url = "http://" + context.installationDao.get.domain + UrlGenerator.url(listing)
    Scalate.layout("/com/cloudray/scalapress/plugin/listings/listingcomplete.ssp", Map("url" -> url))
  }
}
