package com.cloudray.scalapress.plugin.listings.controller.renderer

import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.util.UrlGenerator

/** @author Stephen Samuel */
object ListingCompleteRenderer {

    def render(context: ScalapressContext, listing: Obj) = {
        val url = "http://" + context.installationDao.get.domain + UrlGenerator.url(listing)
        <div id="listing-confirmation-text">
            <p>
                Thank you.
            </p>
            <p>
                Your listing is now completed.
            </p>
            <p>
                When the listing has been verified it will be visible using the following url:
                <br/>
                <a href={url}>
                    {url}
                </a>
            </p>
        </div>
    }
}
