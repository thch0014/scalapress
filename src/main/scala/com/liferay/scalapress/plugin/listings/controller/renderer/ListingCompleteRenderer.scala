package com.liferay.scalapress.plugin.listings.controller.renderer

import com.liferay.scalapress.plugin.friendlyurl.FriendlyUrlGenerator
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.obj.Obj

/** @author Stephen Samuel */
object ListingCompleteRenderer {

    def render(context: ScalapressContext, listing: Obj) = {
        val url = "http://" + context.installationDao.get.domain + FriendlyUrlGenerator.friendlyUrl(listing)
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
